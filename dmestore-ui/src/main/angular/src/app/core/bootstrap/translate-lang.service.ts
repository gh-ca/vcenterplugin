import { Injectable, Injector } from '@angular/core';
import { LOCATION_INITIALIZED } from '@angular/common';
import { TranslateService } from '@ngx-translate/core';
import { SettingsService } from './settings.service';
import {GlobalsService} from "@shared/globals.service";

@Injectable({
  providedIn: 'root',
})
export class TranslateLangService {
  constructor(
    private injector: Injector,
    private translate: TranslateService,
    private settings: SettingsService,
    private gs: GlobalsService
  ) {}

  load() {
    return new Promise<any>((resolve: any) => {
      const locationInitialized = this.injector.get(LOCATION_INITIALIZED, Promise.resolve(null));
      locationInitialized.then(() => {
        const browserLang = navigator.language;
        const defaultLang = browserLang.match(/en-US|zh-CN/) ? browserLang : 'en-US';
        const local = this.gs.getClientSdk().app.getClientLocale();
        console.log('local:', local);
        this.settings.setLanguage(local);
        this.translate.setDefaultLang(local);
        this.translate.use(local).subscribe(
          () => {
            console.log(`Successfully initialized '${local}' language.'`);
          },
          () => {
            console.error(`Problem with '${local}' language initialization.'`);
          },
          () => {
            resolve(null);
          }
        );
      });
    });
  }
}
