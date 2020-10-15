export class FCPort{
  location:string;
  status:string;
  wwn:string;
  speed: number;
  iops:string;
  latency: string;
  bandwidth: string;
  usage: string;
}
export class EthernetPort{
  location:string;
  status:string;
  ipv4:string;
  ipv6: number;
  speed: number;
  iops:string;
  latency: string;
  bandwidth: string;
  usage: string;
}
export class FCoEPort{
  location:string;
  status:string;
  wwn:string;
  speed: number;
  iops:string;
  latency: string;
  bandwidth: string;
  usage: string;
}
export class BondPort{
  name:string;
  health_status:string;
  running_status:string;
  mtu: number;
  iops:string;
  latency: string;
  bandwidth: string;
}
export class LogicPort{
  id: string;
  name: string;
  running_status: string;
  operational_status: string;
  mgmt_ip: string;
  mgmt_ipv6: string;
  home_port_id: string;
  home_port_name: string;
  current_port_id: string;
  current_port_name: string;
  role: string;
  ddns_status: string;
  support_protocol: string;
  management_access: string;
  vstore_id: string;
  vstore_name: string;
}
export class FailoverGroup{
  name: string;
  failover_group_type: string;
}
