package phd.palamedi.finder.impl;

import com.sun.istack.internal.NotNull;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import phd.palamedi.extract.PdfExtract;
import phd.palamedi.finder.ArticleVisitor;
import phd.palamedi.finder.VisitContext;
import phd.palamedi.model.*;
import phd.palamedi.model.Error;
import phd.palamedi.service.ArticleContentService;
import phd.palamedi.service.ArticleService;
import phd.palamedi.service.ErrorService;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by marcos.salomao on 14/12/17.
 */
@Component
public class FileDownloadArticleVisitor implements ArticleVisitor {

    private static final Logger LOGGER = Logger.getLogger(FileDownloadArticleVisitor.class.toString());

    @Autowired
    private ArticleContentService articleContentService;

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

        List<URLConnection> connections = articleFileConnection.getUrlConnections();

        for (URLConnection connection : connections) {

            ArticleContent articleContent = new ArticleContent();
            articleContent.setArticle(article);

            HttpURLConnection httpConn = (HttpURLConnection) connection;
            String fileURL = httpConn.getURL().toString();

            try {

                LOGGER.info("Downloading article from " + fileURL);
                articleContent.setUrl(fileURL);

                int responseCode = httpConn.getResponseCode();

                // always check HTTP response code first
                if (responseCode == HttpURLConnection.HTTP_OK) {

                    LOGGER.info("File downloaded " + fileURL);

                    // opens input stream from the HTTP connection
                    inputStream = httpConn.getInputStream();
                    articleContent.setContent(pdfExtract.extract(inputStream));
                    articleContent.setStatus(Status.OK);

                } else {

                    articleContent.setStatus(Status.ERROR);
                    String message = "Não foi possível o download da URL '"
                            + fileURL
                            + "'.\nArtigo '"
                            + article.getUrl()
                            + "'.\nPublicação '"
                            + article.getPublication().getUrl()
                            + "'. HTTP code: " + responseCode;
                    LOGGER.severe(message);
                    saveException(articleFileContext, message);

                }

            } catch (Exception e) {

                articleContent.setStatus(Status.ERROR);
                String message = "Não foi possível o download da URL '"
                        + fileURL
                        + "'.\nArtigo '"
                        + article.getUrl()
                        + "'.\nPublicação '"
                        + article.getPublication().getUrl()
                        + "'.\nErro: " + e.getMessage();
                LOGGER.severe(message);
                saveException(articleFileContext, message, e);

            } finally {

                try {

                    this.articleContentService.save(articleContent);

                } catch (Exception e) {

                    String message = "Não foi possível salvar o conteúdo da URL '"
                            + fileURL
                            + "'.\nArtigo '"
                            + article.getTitle()
                            + "'.\nPublicação '"
                            + article.getPublication().getName()
                            + ".\nErro: "
                            + e.getMessage();
                    LOGGER.severe(message);
                    this.saveException(articleFileContext, message, e);

                }

                try {

                    if (inputStream != null) {
                        inputStream.close();
                    }

                } catch (IOException e) {

                    String message = "Inputstream não encerrado apropriadamente.";
                    LOGGER.severe(message);
                    this.saveException(articleFileContext, message, e);

                }

                httpConn.disconnect();
            }

        }

    }

    private void saveException(ArticleFileContext articleFileContext, String message) {
        this.saveException(articleFileContext, message, null);
    }

    private void saveException(ArticleFileContext articleFileContext, String message, Exception e) {
        LOGGER.severe(message);
        this.errorService.save(articleFileContext.getArticle().getPublication().getPublisher(), message, e);

    }

}
