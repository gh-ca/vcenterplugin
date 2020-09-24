/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
import * as tslib_1 from "tslib";
/**
 * @param {?} expression
 * @param {?} argNames
 * @return {?}
 */
export function evalStringExpression(expression, argNames) {
    try {
        if (expression.indexOf('this.field') !== -1) {
            console.warn("NgxFormly: using 'this.field' in expressionProperties is deprecated since v5.1, use 'field' instead.");
        }
        return (/** @type {?} */ (Function.apply(void 0, tslib_1.__spread(argNames, ["return " + expression + ";"]))));
    }
    catch (error) {
        console.error(error);
    }
}
/**
 * @param {?} expression
 * @param {?} argNames
 * @return {?}
 */
export function evalExpressionValueSetter(expression, argNames) {
    try {
        return (/** @type {?} */ (Function.apply(void 0, tslib_1.__spread(argNames, [expression + " = expressionValue;"]))));
    }
    catch (error) {
        console.error(error);
    }
}
/**
 * @param {?} expression
 * @param {?} thisArg
 * @param {?} argVal
 * @return {?}
 */
export function evalExpression(expression, thisArg, argVal) {
    if (expression instanceof Function) {
        return expression.apply(thisArg, argVal);
    }
    else {
        return expression ? true : false;
    }
}
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoidXRpbHMuanMiLCJzb3VyY2VSb290Ijoibmc6Ly9Abmd4LWZvcm1seS9jb3JlLyIsInNvdXJjZXMiOlsibGliL2V4dGVuc2lvbnMvZmllbGQtZXhwcmVzc2lvbi91dGlscy50cyJdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiOzs7Ozs7Ozs7O0FBQUEsTUFBTSxVQUFVLG9CQUFvQixDQUFDLFVBQWtCLEVBQUUsUUFBa0I7SUFDekUsSUFBSTtRQUNGLElBQUksVUFBVSxDQUFDLE9BQU8sQ0FBQyxZQUFZLENBQUMsS0FBSyxDQUFDLENBQUMsRUFBRTtZQUMzQyxPQUFPLENBQUMsSUFBSSxDQUFDLHNHQUFzRyxDQUFDLENBQUM7U0FDdEg7UUFFRCxPQUFPLG1CQUFBLFFBQVEsZ0NBQUksUUFBUSxHQUFFLFlBQVUsVUFBVSxNQUFHLEtBQVEsQ0FBQztLQUM5RDtJQUFDLE9BQU8sS0FBSyxFQUFFO1FBQ2QsT0FBTyxDQUFDLEtBQUssQ0FBQyxLQUFLLENBQUMsQ0FBQztLQUN0QjtBQUNILENBQUM7Ozs7OztBQUVELE1BQU0sVUFBVSx5QkFBeUIsQ0FBQyxVQUFrQixFQUFFLFFBQWtCO0lBQzlFLElBQUk7UUFDRixPQUFPLG1CQUFBLFFBQVEsZ0NBQUksUUFBUSxHQUFLLFVBQVUsd0JBQXFCLEtBQXlCLENBQUM7S0FDMUY7SUFBQyxPQUFPLEtBQUssRUFBRTtRQUNkLE9BQU8sQ0FBQyxLQUFLLENBQUMsS0FBSyxDQUFDLENBQUM7S0FDdEI7QUFDSCxDQUFDOzs7Ozs7O0FBRUQsTUFBTSxVQUFVLGNBQWMsQ0FBQyxVQUF1QyxFQUFFLE9BQVksRUFBRSxNQUFhO0lBQ2pHLElBQUksVUFBVSxZQUFZLFFBQVEsRUFBRTtRQUNsQyxPQUFPLFVBQVUsQ0FBQyxLQUFLLENBQUMsT0FBTyxFQUFFLE1BQU0sQ0FBQyxDQUFDO0tBQzFDO1NBQU07UUFDTCxPQUFPLFVBQVUsQ0FBQyxDQUFDLENBQUMsSUFBSSxDQUFDLENBQUMsQ0FBQyxLQUFLLENBQUM7S0FDbEM7QUFDSCxDQUFDIiwic291cmNlc0NvbnRlbnQiOlsiZXhwb3J0IGZ1bmN0aW9uIGV2YWxTdHJpbmdFeHByZXNzaW9uKGV4cHJlc3Npb246IHN0cmluZywgYXJnTmFtZXM6IHN0cmluZ1tdKSB7XG4gIHRyeSB7XG4gICAgaWYgKGV4cHJlc3Npb24uaW5kZXhPZigndGhpcy5maWVsZCcpICE9PSAtMSkge1xuICAgICAgY29uc29sZS53YXJuKGBOZ3hGb3JtbHk6IHVzaW5nICd0aGlzLmZpZWxkJyBpbiBleHByZXNzaW9uUHJvcGVydGllcyBpcyBkZXByZWNhdGVkIHNpbmNlIHY1LjEsIHVzZSAnZmllbGQnIGluc3RlYWQuYCk7XG4gICAgfVxuXG4gICAgcmV0dXJuIEZ1bmN0aW9uKC4uLmFyZ05hbWVzLCBgcmV0dXJuICR7ZXhwcmVzc2lvbn07YCkgYXMgYW55O1xuICB9IGNhdGNoIChlcnJvcikge1xuICAgIGNvbnNvbGUuZXJyb3IoZXJyb3IpO1xuICB9XG59XG5cbmV4cG9ydCBmdW5jdGlvbiBldmFsRXhwcmVzc2lvblZhbHVlU2V0dGVyKGV4cHJlc3Npb246IHN0cmluZywgYXJnTmFtZXM6IHN0cmluZ1tdKSB7XG4gIHRyeSB7XG4gICAgcmV0dXJuIEZ1bmN0aW9uKC4uLmFyZ05hbWVzLCBgJHtleHByZXNzaW9ufSA9IGV4cHJlc3Npb25WYWx1ZTtgKSBhcyAodmFsdWU6IGFueSkgPT4gdm9pZDtcbiAgfSBjYXRjaCAoZXJyb3IpIHtcbiAgICBjb25zb2xlLmVycm9yKGVycm9yKTtcbiAgfVxufVxuXG5leHBvcnQgZnVuY3Rpb24gZXZhbEV4cHJlc3Npb24oZXhwcmVzc2lvbjogc3RyaW5nIHwgRnVuY3Rpb24gfCBib29sZWFuLCB0aGlzQXJnOiBhbnksIGFyZ1ZhbDogYW55W10pOiBhbnkge1xuICBpZiAoZXhwcmVzc2lvbiBpbnN0YW5jZW9mIEZ1bmN0aW9uKSB7XG4gICAgcmV0dXJuIGV4cHJlc3Npb24uYXBwbHkodGhpc0FyZywgYXJnVmFsKTtcbiAgfSBlbHNlIHtcbiAgICByZXR1cm4gZXhwcmVzc2lvbiA/IHRydWUgOiBmYWxzZTtcbiAgfVxufVxuIl19