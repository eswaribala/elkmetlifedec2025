package com.siemens.elasticclient.services;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.siemens.elasticclient.configurations.SecureESClientFactory;
import com.siemens.elasticclient.models.Employee;
import org.springframework.stereotype.Service;
import co.elastic.clients.transport.endpoints.BooleanResponse;

@Service
public class IndexService {

    public static void createIndex() throws Exception {
        ElasticsearchClient client = SecureESClientFactory.createClient();

        String indexName = "employees";

        // 1. Check if index exists
        BooleanResponse exists = client.indices().exists(e -> e.index(indexName));
        if (Boolean.TRUE.equals(exists.value())) {
            System.out.println("Index '" + indexName + "' already exists.");
        } else {
            // 2. Create index (simple dynamic mapping)
            client.indices().create(c -> c.index(indexName));
            System.out.println("Created index '" + indexName + "'.");
        }

        // 3. Index some sample documents
        indexSampleDocs(client, indexName);

        System.out.println("Sample documents indexed into '" + indexName + "'.");
    }
    private static void indexSampleDocs(ElasticsearchClient client, String indexName) throws Exception {
        Employee e1 = new Employee("Alice", "IT", 80000);
        Employee e2 = new Employee("Bob", "IT", 90000);
        Employee e3 = new Employee("Charlie", "HR", 60000);

        client.index(i -> i.index(indexName).id("1").document(e1));
        client.index(i -> i.index(indexName).id("2").document(e2));
        client.index(i -> i.index(indexName).id("3").document(e3));
    }
}
