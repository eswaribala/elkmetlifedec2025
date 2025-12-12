package com.siemens.elasticclient;

import com.siemens.elasticclient.configurations.SecureESClientFactory;
import com.siemens.elasticclient.models.Employee;
import com.siemens.elasticclient.services.IndexService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

import java.util.List;
@SpringBootApplication
public class ElasticclientApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ElasticclientApplication.class, args);
        IndexService.createIndex();

            ElasticsearchClient client = SecureESClientFactory.createClient();

            // 1. Search with aggregations
            SearchResponse<Employee> response = client.search(s -> s
                            .index("employees")
                            .query(q -> q
                                    .term(t -> t
                                            .field("department.keyword")
                                            .value("IT")
                                    )
                            )
                            // Metric agg: average salary
                            .aggregations("avg_salary", a -> a
                                    .avg(avg -> avg.field("salary"))
                            )
                            // Bucket agg: number of docs per department
                            .aggregations("employees_by_dept", a -> a
                                    .terms(t -> t.field("department.keyword"))
                            ),
                    Employee.class);

            // 2. Print hits
            System.out.println("=== Search Hits (IT Department) ===");
            for (Hit<Employee> hit : response.hits().hits()) {
                Employee emp = hit.source();
                if (emp != null) {
                    System.out.printf("Name: %s, Dept: %s, Salary: %.2f%n",
                            emp.getName(), emp.getDepartment(), emp.getSalary());
                }
            }

            // 3. Read aggregations
            System.out.println("\n=== Aggregations ===");

            // 3a. Average salary
            Double avgSalary = response.aggregations()
                    .get("avg_salary").avg().value();
            System.out.println("Average salary (IT): " + avgSalary);

            // 3b. Terms aggregation
            Aggregate deptAgg = response.aggregations().get("employees_by_dept");
            List<StringTermsBucket> buckets = deptAgg.sterms().buckets().array();

            System.out.println("\nEmployees by department:");
            for (StringTermsBucket bucket : buckets) {
                String dept = bucket.key().stringValue();
                long count = bucket.docCount();
                System.out.printf("Dept: %s, Count: %d%n", dept, count);
            }
        }

      

}
