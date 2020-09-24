import { Observable } from 'rxjs';
export interface ClrDatagridFilterInterface<T, S = any> {
    isActive(): boolean;
    accepts(item: T): boolean;
    changes: Observable<any>;
    readonly state?: S;
    equals?(other: ClrDatagridFilterInterface<T, any>): boolean;
}
