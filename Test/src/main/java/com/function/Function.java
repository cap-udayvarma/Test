package com.function;

import com.kroger.eprn.hmsdeltaload.prescriber.HMSDeltaAffinityBatch;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import com.techrx.model.util.batch.BatchContext;
import com.techrx.model.util.batch.BatchProcess;
import com.techrx.model.util.batch.BatchLauncher;

import java.util.Optional;

import org.apache.logging.log4j.core.config.Configurator;

/**
 * Azure Functions with HTTP Trigger.
 */
public class Function {
    /**
     * This function listens at endpoint "/api/HttpExample". Two ways to invoke it using "curl" command in bash:
     * 1. curl -d "HTTP Body" {your host}/api/HttpExample
     * 2. curl "{your host}/api/HttpExample?name=HTTP%20Query"
     */
    @FunctionName("HttpExample")
    public HttpResponseMessage run(
            @HttpTrigger(
                name = "req",
                methods = {HttpMethod.GET},
                authLevel = AuthorizationLevel.ANONYMOUS)
                HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request.");

        try {
           
            String blobName1 = "C:/Users/CAP5634/Downloads/prescriber_address_merge_esv.sh/Properties_files/Edited/DataSourceProperties.properties";
            String blobName2 = "C:/Users/CAP5634/Downloads/prescriber_address_merge_esv.sh/Properties_files/Edited/HMSGlobalLogging.properties";
            String blobName3 = "C:/Users/CAP5634/Downloads/prescriber_address_merge_esv.sh/Properties_files/Edited/batch.properties";

            Configurator.setLevel("com.kroger.eprn.hmsdeltaload.prescriber.HMSDeltaAffinityBatch",
                        org.apache.logging.log4j.Level.DEBUG);
            // String[] args = {blobName1};
            // // BatchProperties ex= new BatchProperties(blobName2);
           
 
            //   BatchMinMaxLoader.main(args);
            //   Configurator.setLevel("com.kroger.esps.minmaxloader.BatchMinMaxLoader",
            //             org.apache.logging.log4j.Level.DEBUG);
 
            // BatchLauncher batchLauncher = new BatchLauncher();
            // BatchContext batchContext = batchLauncher.createBatchContext();
            // batchContext.setProperty("techrx.config.filename",  blobName2);
            // batchContext.setProperty("techrx.common.config.filename", blobName1);
 
            // BatchProperties s = new BatchProperties(blobName1);
                BatchLauncher batchLauncher = new BatchLauncher();
                //String basePath = PropertyFileDownloader.DownloadPropertyFiles(files, context);
                
                BatchContext batchContext = batchLauncher.createBatchContext();
                System.setProperty(BatchProcess.CONFIG_FILE_NAME, blobName1);
                System.setProperty("techrx.common.config.filename", blobName3);
                System.setProperty("oracle.jdbc.autoCommitSpecCompliant", "false");

                System.setProperty("techrx.transType", "N");
                System.setProperty("techrx.staged.replication", "N");
                System.setProperty("techrx.maxNumFetch", "500");
                System.setProperty("esps.facilityNum", "100");

                System.setProperty("maxNumberOfWorkerThreads", "20");
                System.setProperty("esvDeltaId", "20231219");
                System.setProperty("isPrescriberAddressMerge", "Y");
                System.setProperty("java.util.logging.config.file",blobName2);



              //  AutoRetryReAdjItems autoRetryReAdj = new AutoRetryReAdjItems();
              HMSDeltaAffinityBatch hmsDeltaLoad = new HMSDeltaAffinityBatch();
               // Configurator.setLevel("com.techrx.app.trexone.batch.autoretry", org.apache.logging.log4j.Level.DEBUG);
                int result = hmsDeltaLoad.startBatch(batchContext);
                context.getLogger().info("result"+result);
 
            // Return a success response
            return request.createResponseBuilder(HttpStatus.OK)
                    .body("hmsDeltaLoad executed successfully with result: "+ result )
                    .build();
        } catch (Throwable e) {
            // Handle exceptions
            context.getLogger().severe("Error executing hmsDeltaLoad: " + e.getMessage());
 
            // Return an error response
            return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error executing hmsDeltaLoad: " + e.getMessage())
                    .build();
        }
    }
    }