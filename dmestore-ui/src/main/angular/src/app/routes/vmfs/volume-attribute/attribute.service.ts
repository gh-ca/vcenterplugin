import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class AttributeService {
  constructor(private http: HttpClient) {}

  getData(storageObjectId: string) {
    return this.http.get('accessvmfs/volume/' + storageObjectId );
  }
}
export class VolumeInfo{
  name: string;
  wwn: string;
  storage: string;
  storagePool: string;
  serviceLevel: string;
  controlPolicy: string;
  trafficControl: string;
  smartTier: string;
  applicationType: string;
  provisionType: string;
  dudeplication: boolean;
  compression: boolean;
}
