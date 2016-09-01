package org.activiti;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProcessResponseExecutionListener implements ExecutionListener {

    private ObjectMapper objectMapper = new ObjectMapper();

    public void notify(DelegateExecution execution) {

        List<String> searchResults = new ArrayList<String>();

        String response = (String) execution.getVariable("response");
        try {
            JsonNode jsonNode = objectMapper.readTree(response);
            JsonNode itemsNode = jsonNode.get("items");
            if (itemsNode != null && itemsNode.isArray()) {
                for (JsonNode itemNode : (ArrayNode) itemsNode) {
                    String url = itemNode.get("html_url").asText();
                    searchResults.add(url);

                    if (searchResults.size() == 10) {
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        execution.setTransientVariable("searchResults", searchResults);
    }

}
