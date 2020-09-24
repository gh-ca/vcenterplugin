import { OnDestroy } from '@angular/core';
import { ResponsiveNavigationService } from './providers/responsive-navigation.service';
import { ResponsiveNavCodes } from './responsive-nav-codes';
import { ClrCommonStringsService } from '../../utils/i18n/common-strings.service';
export declare class ClrHeader implements OnDestroy {
    private responsiveNavService;
    commonStrings: ClrCommonStringsService;
    isNavLevel1OnPage: boolean;
    isNavLevel2OnPage: boolean;
    openNavLevel: number;
    responsiveNavCodes: typeof ResponsiveNavCodes;
    private _subscription;
    constructor(responsiveNavService: ResponsiveNavigationService, commonStrings: ClrCommonStringsService);
    resetNavTriggers(): void;
    initializeNavTriggers(navList: number[]): void;
    closeOpenNav(): void;
    toggleNav(navLevel: number): void;
    ngOnDestroy(): void;
}
