curl -XDELETE "localhost:9200/elastic2/"
curl -XPUT "localhost:9200/elastic2/"
curl -XPUT "localhost:9200/elastic2/_mappings/date" -d '{ "date": { "properties": { "romantic_date": {"type":"date","format":"yyyy-MM-dd"}}}}'
