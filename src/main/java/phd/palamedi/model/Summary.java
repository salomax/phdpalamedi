package phd.palamedi.model;

import phd.palamedi.finder.impl.SummaryItem;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marcos.salomao on 15/12/17.
 */
@JacksonXmlRootElement
public class Summary {

    @JacksonXmlElementWrapper(localName = "summary", useWrapping = false)
    private List<SummaryItem> summaryItems = new ArrayList<>();
    private List<String> messages = new ArrayList<>();

    public void addItem(SummaryItem summaryItem) {
        summaryItems.add(summaryItem);
    }

    public void addException(String message) {
        this.messages.add(message);
    }

    public List<SummaryItem> getSummaryItems() {
        return summaryItems;
    }

    public List<String> getMessages() {
        return messages;
    }

}
