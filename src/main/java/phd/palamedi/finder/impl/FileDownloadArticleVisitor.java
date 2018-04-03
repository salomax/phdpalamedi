package phd.palamedi.finder.impl;

import com.sun.istack.internal.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import phd.palamedi.extract.PdfExtract;
import phd.palamedi.finder.ArticleVisitor;
import phd.palamedi.finder.VisitContext;
import phd.palamedi.model.Article;
import phd.palamedi.model.ArticleFileConnection;
import phd.palamedi.model.Error;
import phd.palamedi.model.Status;
import phd.palamedi.service.ArticleService;
import phd.palamedi.service.ErrorService;

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

    private static final Logger LOGGER = Logger.getLogger(FileDownloadArticleVisitor.class.toString());

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ErrorService errorService;

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

        Article article = articleFileContext.getArticle();

        ArticleFileConnection articleFileConnection = articleFileContext.get();

        HttpURLConnection httpConn = (HttpURLConnection) articleFileConnection.getUrlConnection();
        String fileURL = httpConn.getURL().toString();

        try {

            LOGGER.info("Downloading article from " + fileURL);

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

                saveException(articleFileContext, "No file to download. Server replied HTTP code: " + responseCode);

            }

        } catch (Exception e) {

            saveException(articleFileContext, "Download error " + fileURL, e);

        } finally {

            try {

                this.articleService.save(article);

            } catch (Exception e) {

                this.saveException(articleFileContext, "Article not saved " + fileURL, e);

            }

            try {

                if (inputStream != null) {
                    inputStream.close();
                }

            } catch (IOException e) {

                this.saveException(articleFileContext, "Inputstream not saved " + fileURL, e);

            }

            httpConn.disconnect();
        }

    }

    private void saveException(ArticleFileContext articleFileContext, String message) {
        this.saveException(articleFileContext, message, null);
    }

    private void saveException(ArticleFileContext articleFileContext, String message, Exception e) {
        LOGGER.severe(message);
        Error error = new Error();
        error.setPublisher(articleFileContext.getArticle().getPublication().getPublisher());
        error.setMessage(message);
        error.setException(org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e));
        this.errorService.save(error);

    }

    private String getFileName(String contentDisposition) throws UnsupportedEncodingException {
        String fileName = contentDisposition.replaceFirst("(?i)^.*filename=\"?([^\"]+)\"?.*$", "$1");
        return URLDecoder.decode(fileName, "ISO_8859_1");
    }

}
