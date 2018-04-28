package phd.palamedi.controller;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsxView;
import phd.palamedi.response.ArticleResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by marcos.salomao on 28/4/18.
 */
public class ArticleExcelView extends AbstractXlsxView {

    private List<ArticleResponse> articles;

    public ArticleExcelView(List<ArticleResponse> articles) {
        this.articles = articles;
    }

    @Override
    protected void buildExcelDocument(Map<String, Object> map,
                                      Workbook workbook,
                                      HttpServletRequest httpServletRequest,
                                      HttpServletResponse httpServletResponse) throws Exception {

        Sheet sheet = workbook.createSheet("Resultado");

        int column = 0;
        int rowCount = 0;

        Row header = sheet.createRow(rowCount++);
        header.createCell(column++).setCellValue("Periódico");
        header.createCell(column++).setCellValue("Publicação");
        header.createCell(column++).setCellValue("Ano");
        header.createCell(column++).setCellValue("Título");
        header.createCell(column++).setCellValue("Autor");
        header.createCell(column++).setCellValue("Resumo");
        header.createCell(column++).setCellValue("Palavras Chaves");
        header.createCell(column++).setCellValue("URL");
        header.createCell(column++).setCellValue("Tags");

        for(ArticleResponse article : articles){
            column = 0;
            Row row =  sheet.createRow(rowCount++);
            row.createCell(column++).setCellValue(getValue(article.getPublication().getPublisher()));
            row.createCell(column++).setCellValue(getValue(article.getPublication().getName()));
            row.createCell(column++).setCellValue(getValue(article.getPublication().getYear()));
            row.createCell(column++).setCellValue(getValue(article.getTitle()));
            row.createCell(column++).setCellValue(getValue(article.getAuthor()));
            row.createCell(column++).setCellValue(getValue(article.getSummary()));
            row.createCell(column++).setCellValue(getValue(article.getKeywords()));
            row.createCell(column++).setCellValue(getValue(article.getUrl()));
            row.createCell(column++).setCellValue(getTags(article.getTags()));
        }

    }

    private String getTags(List<String> tags) {
        if (tags != null) {
            return StringUtils.join(tags, ", ");
        }
        return "";
    }

    private String getValue(Object value) {
        return value != null ? value.toString() : "";
    }

}
