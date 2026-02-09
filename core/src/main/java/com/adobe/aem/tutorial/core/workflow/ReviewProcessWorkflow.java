package com.adobe.aem.tutorial.core.workflow;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.commons.mail.MailTemplate;
import com.day.cq.mailer.MessageGateway;
import com.day.cq.mailer.MessageGatewayService;
import org.apache.commons.lang.text.StrLookup;
import org.apache.commons.mail.HtmlEmail;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.jcr.Node;
import java.util.HashMap;
import java.util.Map;

@Component(
        service = WorkflowProcess.class,
        property = {
                "process.label= Review Process Email Notification"
        }
)
public class ReviewProcessWorkflow implements WorkflowProcess {
    private static final Logger LOG = LoggerFactory.getLogger(ReviewProcessWorkflow.class);
    private static final String PUBLISH_EMAIL_TEMPLATE = "/content/dam/practice/email-templates/pagepublishedemailtemplate.txt";
    private static final String EMAIL_SUBJECT = "Page Reviewd and Published";
    private static final String EMAIL_RECIPIENT = "marceloabreu1997@hotmail.com";
    private static final String DOMAIN = "http://localhost://4502";

    @Reference
    MessageGatewayService messageGatewayService;

    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap) throws WorkflowException {
        LOG.info("Executing custom workflow process step");

        WorkflowData workflowData = workItem.getWorkflowData();
        String payloadPath = workflowData.getPayload().toString();
        ResourceResolver resourceResolver = workflowSession.adaptTo(ResourceResolver.class);

        LOG.info("Payload path: {}", payloadPath);
        try{
            if(payloadPath != null){
                String pageLink = DOMAIN + payloadPath + "html";
                Map<String,String> parameters = new HashMap<>();
                parameters.put("pageLink", pageLink);
                Resource templateResource = resourceResolver.getResource(PUBLISH_EMAIL_TEMPLATE + "/jcr:content");
                if(templateResource != null){
                    Node templateNode = templateResource.adaptTo(Node.class);
                    if(templateNode != null){
                        MailTemplate mailTemplate = MailTemplate.create(PUBLISH_EMAIL_TEMPLATE, templateNode.getSession());
                        HtmlEmail email = mailTemplate.getEmail(StrLookup.mapLookup(parameters),HtmlEmail.class);
                        email.setSubject(EMAIL_SUBJECT);
                        email.addTo(EMAIL_RECIPIENT.split(","));
                        MessageGateway<HtmlEmail> messageGateway = messageGatewayService.getGateway(HtmlEmail.class);
                        if(messageGateway != null){
                            messageGateway.send(email);
                        }else{
                            LOG.error("Error in Sending email");
                        }
                    }
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
