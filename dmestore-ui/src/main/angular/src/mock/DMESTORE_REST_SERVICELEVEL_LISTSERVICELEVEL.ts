export default {
  "code": "200",
  "data": [{
    "capabilities": {
      "resourceType": "thin",
      "compression": "disabled",
      "deduplication": "disabled",
      "smarttier": {
        "enabled": true,
        "policy": 2
      },
      "iopriority": null,
      "qos": {
        "smartQos": null,
        "qosParam": {
          "enabled": null,
          "latency": 31,
          "latencyUnit": "ms",
          "minBandWidth": 21,
          "minIOPS": 201,
          "maxBandWidth": 0,
          "maxIOPS": 0,
          "smartQos": null
        },
        "enabled": true
      }
    },
    "id": "0949e53b-77a0-4c4b-a344-d66142dbc85f",
    "name": "D存储策略2",
    "description": "block service-level for dj",
    "type": "BLOCK",
    "protocol": null,
    "totalCapacity": 2208768.0,
    "freeCapacity": 1982592.0,
    "usedCapacity": 226176.0
  }, {
    "capabilities": {
      "resourceType": null,
      "compression": null,
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
          "maxBandWidth": 125,
          "maxIOPS": 255,
          "smartQos": null
        },
        "enabled": true
      }
    },
    "id": "8ae0174e-9ac7-4ca9-bc6c-4d38f0b592b2",
    "name": "t_service_level",
    "description": "block service-level for dj",
    "type": "BLOCK",
    "protocol": null,
    "totalCapacity": 10240.0,
    "freeCapacity": 6592.0,
    "usedCapacity": 3648.0
  }, {
    "capabilities": {
      "resourceType": "thin",
      "compression": "enabled",
      "deduplication": "enabled",
      "smarttier": null,
      "iopriority": {
        "enabled": true,
        "policy": 1
      },
      "qos": null
    },
    "id": "6ab81a6f-e552-45a0-9b15-b3718282e8bb",
    "name": "lq_service_level",
    "description": "block service-level for dj",
    "type": "BLOCK",
    "protocol": null,
    "totalCapacity": 2208768.0,
    "freeCapacity": 1982592.0,
    "usedCapacity": 226176.0
  }],
  "description": null
}
