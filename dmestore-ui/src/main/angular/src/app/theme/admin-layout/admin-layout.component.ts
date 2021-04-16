import {
  Component,
  OnInit,
  OnDestroy,
  ViewChild,
  HostBinding,
  ElementRef,
  Inject,
  Optional,
  ViewEncapsulation,
} from '@angular/core';
import {DOCUMENT} from '@angular/common';
import {NavigationEnd, Router} from '@angular/router';
import {Subscription} from 'rxjs';
import {BreakpointObserver} from '@angular/cdk/layout';
import {OverlayContainer} from '@angular/cdk/overlay';
import {Directionality} from '@angular/cdk/bidi';
import {MatSidenav, MatSidenavContent} from '@angular/material/sidenav';
import {environment} from '@env/environment';

import {SettingsService, AppSettings} from '@core';
import {AppDirectionality} from '@shared';
import {GlobalsService} from "../../shared/globals.service";

const MOBILE_MEDIAQUERY = 'screen and (max-width: 599px)';
const TABLET_MEDIAQUERY = 'screen and (min-width: 600px) and (max-width: 959px)';
const MONITOR_MEDIAQUERY = 'screen and (min-width: 960px)';

@Component({
  selector: 'app-admin-layout',
  templateUrl: './admin-layout.component.html',
  styleUrls: ['./admin-layout.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class AdminLayoutComponent implements OnInit, OnDestroy {
  //@ViewChild('sidenav', { static: true }) sidenav: MatSidenav;
  //@ViewChild('content', { static: true }) content: MatSidenavContent;
  env = environment
  options = this.settings.getOptions();
  loading = this.gs.loading
  linkInfo

  private layoutChangesSubscription: Subscription;

  private isMobileScreen = false;

  get isOver(): boolean {
    return this.isMobileScreen;
  }

  private contentWidthFix = true;

  @HostBinding('class.matero-content-width-fix') get isContentWidthFix() {
    return (
      this.contentWidthFix &&
      this.options.navPos === 'side' &&
      this.options.sidenavOpened &&
      !this.isOver
    );
  }

  private collapsedWidthFix = true;

  @HostBinding('class.matero-sidenav-collapsed-fix') get isCollapsedWidthFix() {
    return (
      this.collapsedWidthFix &&
      (this.options.navPos === 'top' || (this.options.sidenavOpened && this.isOver))
    );
  }

  constructor(
    private router: Router,
    private breakpointObserver: BreakpointObserver,
    private overlay: OverlayContainer,
    private element: ElementRef,
    private settings: SettingsService,
    @Optional() @Inject(DOCUMENT) private document: Document,
    @Inject(Directionality) public dir: AppDirectionality,
    public gs: GlobalsService
  ) {
    this.dir.value = this.options.dir;
    this.document.body.dir = this.dir.value;
    this.linkInfo = [
      {link: "./dashboard", label: 'Overview'},
      {link: "./storage", label: 'Storage Device'},
      {link: "./storage/detail", label: 'Storage Device detail'},
      {link: "./vmfs/list", label: './vmfs/list'},
      {link: "./vmfs/add", label: './vmfs/add'},
      {link: "./vmfs/performance", label: './vmfs/performance'},
      {link: "./vmfs/unmount", label: './vmfs/unmount'},
      {link: "./nfs", label: 'NFS Datastore'},
      {link: "./nfs/list", label: './nfs/list'},
      {link: "./servicelevel", label: 'servicelevel DME存储策略'},
      {link: "./bestpractice", label: 'Best Practice'},
      {link: './best/host/applybp', label: 'host 实施最佳实践'},
      {link: './best/cluster/applybp', label: 'cluster 实施最佳实践'},
      {link: "./iscsi", label: 'iscsi list'},
      /**/
      {link: './iscsi', label: './iscsi'},
      {link: './vmfs/delete', label: './vmfs/delete'},
      {link: './nfs/delete', label: './nfs/delete'},
      {link: './vmfs/expand?resource=dataStore', label: './vmfs/expand?resource=dataStore'},
      {link: './vmfs/serviceLevel?resource=dataStore', label: './vmfs/serviceLevel?resource=dataStore'},
      {link: './vmfs/modify?resource=dataStore', label: './vmfs/modify?resource=dataStore'},
      {link: './vmfs/mount?resource=dataStore', label: './vmfs/mount?resource=dataStore'},
      {link: './vmfs/unmount?resource=dataStore', label: './vmfs/unmount?resource=dataStore'},
      {link: './vmfs/reclaim?resource=dataStore', label: './vmfs/reclaim?resource=dataStore'},
      {link: './nfs/expand', label: './nfs/expand'},
      {link: './nfs/reduce', label: './nfs/reduce'},
      {link: './nfs/modify', label: './nfs/modify'},
      {link: './nfs/dataStore/mount', label: './nfs/dataStore/mount'},
      {link: './nfs/dataStore/unmount', label: './nfs/dataStore/unmount'},
      {link: './vmfs/host/mount?resource=others', label: './vmfs/host/mount?resource=others'},
      {link: './vmfs/host/unmount?resource=others', label: './vmfs/host/unmount?resource=others'},
      {link: './nfs/host/mount', label: './nfs/host/mount'},
      {link: './nfs/host/unmount', label: './nfs/host/unmount'},
      {link: './nfs/add', label: './nfs/add'},
      {link: './vmfs/add?resource=others', label: './vmfs/add?resource=others'},
      {link: './vmfs/cluster/mount?resource=others', label: './vmfs/cluster/mount?resource=others'},
      {link: './vmfs/cluster/unmount?resource=others', label: './vmfs/cluster/unmount?resource=others'},
      {link: './nfs/cluster/mount', label: './nfs/cluster/mount'},
      {link: './nfs/cluster/unmount', label: './nfs/cluster/unmount'},
      {link: './best/cluster/applybp', label: './best/cluster/applybp'},
      {link: './nfs/add', label: './nfs/add'},
      {link: './vmfs/add?resource=others', label: './vmfs/add?resource=others'},
      {link: './rdm', label: './rdm'},
    ]
    this.layoutChangesSubscription = this.breakpointObserver
      .observe([MOBILE_MEDIAQUERY, TABLET_MEDIAQUERY, MONITOR_MEDIAQUERY])
      .subscribe(state => {
        // SidenavOpened must be reset true when layout changes
        this.options.sidenavOpened = true;

        this.isMobileScreen = state.breakpoints[MOBILE_MEDIAQUERY];
        this.options.sidenavCollapsed = state.breakpoints[TABLET_MEDIAQUERY];
        this.contentWidthFix = state.breakpoints[MONITOR_MEDIAQUERY];
      });

    // TODO: Scroll top to container
    this.router.events.subscribe(evt => {
      if (evt instanceof NavigationEnd) {
        // this.content.scrollTo({ top: 0 });
      }
    });
  }

  ngOnInit() {
    setTimeout(() => (this.contentWidthFix = this.collapsedWidthFix = false));
  }

  ngOnDestroy() {
    this.layoutChangesSubscription.unsubscribe();
  }

  toggleCollapsed() {
    this.options.sidenavCollapsed = !this.options.sidenavCollapsed;
    this.resetCollapsedState();
  }

  resetCollapsedState(timer = 400) {
    // TODO: Trigger when transition end
    setTimeout(() => {
      this.settings.setNavState('collapsed', this.options.sidenavCollapsed);
    }, timer);
  }
}
