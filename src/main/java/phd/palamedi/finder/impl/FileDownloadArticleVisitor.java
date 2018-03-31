package phd.palamedi.finder.impl;

import com.sun.istack.internal.NotNull;
import com.uwyn.jhighlight.tools.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import phd.palamedi.crawler.impl.PdfArticleCrawler;
import phd.palamedi.extract.PdfExtract;
import phd.palamedi.finder.ArticleVisitor;
import phd.palamedi.finder.VisitContext;
import phd.palamedi.model.Article;
import phd.palamedi.model.ArticleFileConnection;
import phd.palamedi.model.Status;
import phd.palamedi.repository.ArticleRepository;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLDecoder;
import java.util.logging.Logger;

/**
 * Created by marcos.salomao on 14/12/17.
 */
@Component
public class FileDownloadArticleVisitor implements ArticleVisitor {

    private static final Logger LOGGER = Logger.getLogger(PdfArticleCrawler.class.toString());

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private PdfExtract pdfExtract;

    @Override
    public void visit(@NotNull VisitContext context) {

        if (context instanceof ArticleFileContext) {

            ArticleFileContext articleFileContext = (ArticleFileContext) context;

            // Download file
            this.downloadFile(articleFileContext);

        }


    }

    protected void downloadFile(ArticleFileContext articleFileContext) {

        InputStream inputStream = null;

        Article article = new Article();
        article.setPublisher(articleFileContext.getPublisher());

        ArticleFileConnection articleFileConnection = articleFileContext.get();

        HttpURLConnection httpConn = (HttpURLConnection) articleFileConnection.getUrlConnection();
        String fileURL = httpConn.getURL().toString();

        try {

            article.setUrl(fileURL);

            String fileName = this.getFileName(httpConn.getHeaderField("Content-Disposition"));
            if (StringUtils.isEmpty(fileName)) {
                fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1, fileURL.length());
            }
            article.setFileName(fileName);

            int responseCode = httpConn.getResponseCode();

            // always check HTTP response code first
            if (responseCode == HttpURLConnection.HTTP_OK) {

                LOGGER.info("File downloaded " + fileURL);

                article.setStatus(Status.OK);

                // opens input stream from the HTTP connection
                inputStream = httpConn.getInputStream();
                article.setContent(pdfExtract.extract(inputStream));

            } else {

                article.setStatus(Status.ERROR);
                article.setError("Response: " + httpConn.getResponseCode() + " " + httpConn.getResponseMessage());

                LOGGER.severe("No file to download. Server replied HTTP code: " + responseCode);
            }

        } catch (Exception e) {

            article.setStatus(Status.ERROR);
            article.setError(ExceptionUtils.getExceptionStackTrace(e));

            LOGGER.severe(e.getMessage());

        } finally {

            try {

                this.articleRepository.save(article);

            } catch (Exception e) {

                // Create error article
                article = new Article();
                article.setPublisher(articleFileContext.getPublisher());
                article.setUrl(fileURL);
                article.setStatus(Status.ERROR);
                article.setError(ExceptionUtils.getExceptionStackTrace(e));
                this.articleRepository.save(article);

            }

            try {

                inputStream.close();

            } catch (IOException e) {
                LOGGER.severe(e.getMessage());

            }

            httpConn.disconnect();
        }

    }

    private String getFileName(String contentDisposition) throws UnsupportedEncodingException {
        String fileName = contentDisposition.replaceFirst("(?i)^.*filename=\"?([^\"]+)\"?.*$", "$1");
        return URLDecoder.decode(fileName, "ISO_8859_1");
    }

}
