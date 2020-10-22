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
  healthStatus:string;
  runningStatus:string;
  mtu: number;
  iops:string;
  latency: string;
  bandwidth: string;
}
export class LogicPort{
  id: string;
  name: string;
  runningStatus: string;
  operationalStatus: string;
  mgmtIp: string;
  mgmtIpv6: string;
  homePortId: string;
  homePortName: string;
  currentPortId: string;
  currentPortName: string;
  role: string;
  ddnsStatus: string;
  supportProtocol: string;
  managementAccess: string;
  vstoreId: string;
  vstoreName: string;
}
export class FailoverGroup{
  name: string;
  failoverGroupType: string;
}
