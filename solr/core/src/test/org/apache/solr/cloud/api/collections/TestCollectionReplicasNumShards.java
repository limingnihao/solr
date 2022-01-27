package org.apache.solr.cloud.api.collections;

import org.apache.solr.client.solrj.embedded.JettySolrRunner;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.request.CollectionAdminRequest;
import org.apache.solr.client.solrj.response.CollectionAdminResponse;
import org.apache.solr.cloud.CloudDescriptor;
import org.apache.solr.common.cloud.Slice;
import org.apache.solr.core.SolrCore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestCollectionReplicasNumShards extends ReplicaPropertiesBase {
    public static final int MAX_WAIT_TIMEOUT_SECONDS = 90;
    public static final String COLLECTION_NAME = "testcollection";

    public TestCollectionReplicasNumShards() {
        schemaString = "schema15.xml";      // we need a string id
    }


    @Test
    public void test() throws Exception {
        try (CloudSolrClient client = createCloudClient(null)) {
            int shards = 2;
            int rFactor = 1;
            createCollection(null, COLLECTION_NAME, shards, rFactor, client, null, "conf1");
        }
        waitForCollection(cloudClient.getZkStateReader(), COLLECTION_NAME, 2);
        Map<String, Slice> slices = cloudClient.getZkStateReader().getClusterState().getCollection(COLLECTION_NAME).getSlicesMap();
        List<String> sliceList = new ArrayList<>(slices.keySet());
        String c1_s1 = sliceList.get(0);
        List<String> replicasList = new ArrayList<>(slices.get(c1_s1).getReplicasMap().keySet());
        String c1_s1_r1 = replicasList.get(0);
//        String c1_s1_r2 = replicasList.get(1);

        for (JettySolrRunner solrRunner : jettys) {
            for (SolrCore solrCore : solrRunner.getCoreContainer().getCores()) {
                CloudDescriptor cloudDescriptor = solrCore.getCoreDescriptor().getCloudDescriptor();
                System.out.println(cloudDescriptor.getCoreNodeName() + " - " + cloudDescriptor.getNumShards());
            }
        }
        String nodeName = c1_s1_r1;
        // Add a replica using the "node" parameter
        // this node should have 2 replicas on it
        CollectionAdminResponse addReplica = CollectionAdminRequest.addReplicaToShard(COLLECTION_NAME, c1_s1)
                .setNode(nodeName)
                .process(cloudClient);
        System.out.println(addReplica.isSuccess());
        for (JettySolrRunner solrRunner : jettys) {
            for (SolrCore solrCore : solrRunner.getCoreContainer().getCores()) {
                CloudDescriptor cloudDescriptor = solrCore.getCoreDescriptor().getCloudDescriptor();
                System.out.println(cloudDescriptor.getCoreNodeName() + " - " + cloudDescriptor.getNumShards());
            }
        }

    }

}
