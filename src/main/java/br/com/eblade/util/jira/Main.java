package br.com.eblade.util.jira;

import com.atlassian.jira.rest.client.JiraRestClient;
import com.atlassian.jira.rest.client.JiraRestClientFactory;
import com.atlassian.jira.rest.client.domain.BasicIssue;
import com.atlassian.jira.rest.client.domain.Issue;
import com.atlassian.jira.rest.client.domain.IssueType;
import com.atlassian.jira.rest.client.domain.User;
import com.atlassian.jira.rest.client.domain.input.IssueInput;
import com.atlassian.jira.rest.client.domain.input.IssueInputBuilder;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.atlassian.util.concurrent.Promise;

import java.net.URI;

/**
 * Entry-point invoked when the jar is executed.
 */
public class Main
{
    private static final String JIRA_URL = "https://fittest.atlassian.net";
    private static final String JIRA_ADMIN_USERNAME = "fit";
    private static final String JIRA_ADMIN_PASSWORD = "fittest";

    public static void main(String[] args) throws Exception
    {
        // Print usage instructions
        StringBuilder intro = new StringBuilder();
        intro.append("**********************************************************************************************\r\n");
        intro.append("* JIRA Java REST Client ('JRJC') example.                                                    *\r\n");
        intro.append("* NOTE: Start JIRA using the Atlassian Plugin SDK before running this example.               *\r\n");
        intro.append("* (for example, use 'atlas-run-standalone --product jira --version 6.0 --data-version 6.0'.) *\r\n");
        intro.append("**********************************************************************************************\r\n");
        System.out.println(intro.toString());

        // Construct the JRJC client
        System.out.println(String.format("Logging in to %s with username '%s' and password '%s'", JIRA_URL, JIRA_ADMIN_USERNAME, JIRA_ADMIN_PASSWORD));
        JiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();
        URI uri = new URI(JIRA_URL);
        JiraRestClient client = factory.createWithBasicHttpAuthentication(uri, JIRA_ADMIN_USERNAME, JIRA_ADMIN_PASSWORD);

        // Invoke the JRJC Client
        Promise<User> promise = client.getUserClient().getUser(JIRA_ADMIN_USERNAME);
        User user = promise.claim();

        // Print the result
        System.out.println(String.format("Your admin user's email address is: %s\r\n", user.getEmailAddress()));

        // 2. Access the Issue from TakeBack project in JIRA
        // Take Back Key: TB
        // Issue TB-10948 - Summary: Recebimento 04/09/2014
        // Issue Type: Hardware - Envio Direto Id: 10100
        Promise<Iterable<IssueType>> pAllIssueTypes = client.getMetadataClient().getIssueTypes();
        Iterable<IssueType> allIssueTypes = pAllIssueTypes.claim();
        for (IssueType jIssueType : allIssueTypes) {
        	
        	//if (jIssueType.getName().equals("Supplies")) {               
                Long issueTypeID = jIssueType.getId();
                String issueTypeName = jIssueType.getName();
                String issueTypeDescription = jIssueType.getDescription();
                System.out.println(issueTypeID + " - " + issueTypeName + " - " + issueTypeDescription);
        	//}
        	
        }        
        
        // 3. Insert Issue in Project Take Back
        // Issue ? - Summary: Teste Integração JIRA
        // Issue Type: Supplies Id: 16
        IssueInputBuilder issueBuilder = new IssueInputBuilder("DDM", 9L);
        
        issueBuilder.setSummary("Mais um teste de inclusão!");
        // issueBuilder.setDescription("Testando a integração via JIRA REST Java Client");
        // issueBuilder.setFieldValue("10342", "Teste Integração JIRA");
        
        IssueInput issueInput = issueBuilder.build();
        Promise<BasicIssue> promiseBasicIssue = client.getIssueClient().createIssue(issueInput);
        BasicIssue bIssue = promiseBasicIssue.claim();
        Promise<Issue> promiseJavaIssue = client.getIssueClient().getIssue(bIssue.getKey());
        Issue jIssue = promiseJavaIssue.claim();
        System.out.println(String.format("Your new task is: %s\r\n", jIssue.getSummary()));        
        
        // Done
        System.out.println("Example complete. Now exiting.");
        System.exit(0);
    }
}