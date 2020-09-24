/**
 * Copyright (c) 2016-2019 VMware, Inc. All Rights Reserved.
 * This software is released under MIT license.
 * The full license information can be found in LICENSE in the root directory of this project.
 */
import { OnInit, InjectionToken, OnDestroy } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { DynamicWrapper } from '../../utils/host-wrapping/dynamic-wrapper';
import { ClrLabel } from '../common/label';
export declare const IS_TOGGLE: InjectionToken<BehaviorSubject<boolean>>;
export declare function isToggleFactory(): BehaviorSubject<boolean>;
export declare const IS_TOGGLE_PROVIDER: {
    provide: InjectionToken<BehaviorSubject<boolean>>;
    useFactory: typeof isToggleFactory;
};
export declare class ClrCheckboxWrapper implements DynamicWrapper, OnInit, OnDestroy {
    _dynamic: boolean;
    label: ClrLabel;
    toggle: boolean;
    private subscriptions;
    constructor(toggleService: BehaviorSubject<boolean>);
    ngOnInit(): void;
    ngOnDestroy(): void;
}
