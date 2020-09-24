import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { MenuService } from './menu.service';
import { SettingsService } from './settings.service';

@Injectable({
  providedIn: 'root',
})
export class StartupService {
  constructor(
    private menu: MenuService,
    private http: HttpClient,
    private settings: SettingsService
  ) {}

  load(): Promise<any> {
    return new Promise((resolve, reject) => {
      resolve();
      /* this.http
        .get('assets/data/menu.json?_t=' + Date.now())
        .pipe(
          catchError(res => {
            resolve();
            return throwError(res);
          })
        )
        .subscribe(
          (res: any) => {
            this.menu.recursMenuForTranslation(res.menu, 'menu');
            this.menu.set(res.menu);

            // Refresh user info
            // In a real app, user data will be fetched form API
            this.settings.setUser({
              id: 1,
              name: '',
              email: '',
              avatar: '',
            });
          },
          () => {
            reject();
          },
          () => {
            resolve();
          }
        ); */
    });
  }
}
