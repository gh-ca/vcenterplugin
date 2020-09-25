import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class ShareService {
  constructor(private http: HttpClient) {}

  getData(param) {
    return this.http.get('accessnfs/share/' + param);
  }
}
export class ShareDetail{
  fs_name: string;
  name: string;
  share_path: string;
  description: string;
  owning_dtree_id: string;
  owning_dtree_name: string;
  device_name: string;
  auth_client_list: AuthClient[];
}
export class AuthClient{
   accessval: string;
   all_squash: string;
   id: string;
   name: string;
   parent_id: string;
   root_squash: string;
   secure: string;
   sync: string;
   type: string;
   vstore_id_in_storage: string;
   vstore_name: string;
}

