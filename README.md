# TODO

- [ ] Persist simulation results
- [x] Generate simulation data (1-10-100-1000m)
- [x] include_in_parent
- [ ] Group paths while flattening
- [ ] Save with & without refresh (ES); incl. saveImmediately
- [ ] Lookup ORDER type

```shell
.\mvnw gatling:test "-Dgatling.simulationClass=io.paval.demo.EventPostAndGetSimulation"
.\mvnw gatling:test "-Dgatling.simulationClass=io.paval.demo.EventPostSimulation"
```

# Samples

## 1. Elasticsearch 7.17.28 + single index

| Batch           | Sample Size | Logstash Time | Docs Count[^1] | Storage Size | Initial Query[^2] | Snapshot Time |
|-----------------|-------------|---------------|----------------|--------------|-------------------|---------------|
| 1m              | 2.6 GB      | 5m            | 30m            | 2 GB         | ~5s               | ~30s          |
| 1m + parent[^3] | 2.6 GB      | 5m            | 30m            | 2.2 GB       | ~5s               | ~30s          |
| ~10m            | 26 GB       |               | 330m           | 15 GB        | 36s               | ~6m           |

## 2. Elasticsearch 7.17.28 + data stream

| Batch | Sample Size | Logstash Time | Docs Count[^1] | Storage Size | Initial Query[^2] | Snapshot Time |
|-------|-------------|---------------|----------------|--------------|-------------------|---------------|
| ~1m   | 2.6 GB      | 4m            |                | 2.2 GB       | 4s                |               |

[^1]: Includes nested documents
[^2]: Order counts per all customers (group by email)
[^3]: include_in_parent: true

# Notes

- Properties set as `object` (such as data) don't get indexed even if they show up flattened in Kibana
- when `include_in_parent` is set to true, search queries can be simpler without the nested level being required