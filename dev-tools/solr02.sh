path=$(pwd)

home=/Volumes/Software/Apache/solr_data_10/n2

solrPath=$path/solr/packaging/build/solr-10.0.0-SNAPSHOT

zkhost=localhost:2181

port=8982

portDebug=5002

mkdir $home
mkdir $home/logs

$solrPath/bin/solr start -c -f \
        -s $home \
        -z $zkhost \
        -p $port \
        -m 1g \
        -a -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=$portDebug \
        -Dsolr.solrxml.location=zookeeper \
        -Dsolr.log.dir=$home/logs

