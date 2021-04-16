export default {
  "code": "200",
  "data": [{
    "capabilities": null,
    "id": "c9c48c60-1528-4514-baf4-af6b1e091045",
    "name": "yctest",
    "description": "block service-level for dj",
    "type": "BLOCK",
    "protocol": null,
    "totalCapacity": 2234368.0,
    "freeCapacity": 278336.0,
    "usedCapacity": 1956032.0
  }, {
    "capabilities": {
      "resourceType": "thin",
      "compression": false,
      "deduplication": null,
      "smarttier": null,
      "iopriority": {
        "enabled": true,
        "policy": 2
      },
      "qos": null
    },
    "id": "c5445eae-1ecf-4439-b345-592526c16594",
    "name": "lq_server_level",
    "description": "block service-level for dj",
    "type": "BLOCK",
    "protocol": null,
    "totalCapacity": 10240.0,
    "freeCapacity": 6592.0,
    "usedCapacity": 3648.0
  }, {
    "capabilities": {
      "resourceType": "thin",
      "compression": false,
      "deduplication": null,
      "smarttier": null,
      "iopriority": null,
      "qos": {
        "smartQos": null,
        "qosParam": {
          "enabled": null,
          "latency": 0,
          "latencyUnit": "ms",
          "minBandWidth": 0,
          "minIOPS": 0,
          "maxBandWidth": 175,
          "maxIOPS": 145,
          "smartQos": null
        },
        "enabled": true
      }
    },
    "id": "2784cf0f-9821-4f59-963b-836dbecbd271",
    "name": "D这是存储DME",
    "description": "block service-level for dj",
    "type": "BLOCK",
    "protocol": null,
    "totalCapacity": 2244608.0,
    "freeCapacity": 292224.0,
    "usedCapacity": 1952384.0
  }],
  "description": null
}
