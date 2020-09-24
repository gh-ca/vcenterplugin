/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
/**
 * @param {?} expression
 * @param {?} argNames
 * @return {?}
 */
export function evalStringExpression(expression, argNames) {
    try {
        if (expression.indexOf('this.field') !== -1) {
            console.warn(`NgxFormly: using 'this.field' in expressionProperties is deprecated since v5.1, use 'field' instead.`);
        }
        return (/** @type {?} */ (Function(...argNames, `return ${expression};`)));
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
        return (/** @type {?} */ (Function(...argNames, `${expression} = expressionValue;`)));
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
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoidXRpbHMuanMiLCJzb3VyY2VSb290Ijoibmc6Ly9Abmd4LWZvcm1seS9jb3JlLyIsInNvdXJjZXMiOlsibGliL2V4dGVuc2lvbnMvZmllbGQtZXhwcmVzc2lvbi91dGlscy50cyJdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiOzs7Ozs7Ozs7QUFBQSxNQUFNLFVBQVUsb0JBQW9CLENBQUMsVUFBa0IsRUFBRSxRQUFrQjtJQUN6RSxJQUFJO1FBQ0YsSUFBSSxVQUFVLENBQUMsT0FBTyxDQUFDLFlBQVksQ0FBQyxLQUFLLENBQUMsQ0FBQyxFQUFFO1lBQzNDLE9BQU8sQ0FBQyxJQUFJLENBQUMsc0dBQXNHLENBQUMsQ0FBQztTQUN0SDtRQUVELE9BQU8sbUJBQUEsUUFBUSxDQUFDLEdBQUcsUUFBUSxFQUFFLFVBQVUsVUFBVSxHQUFHLENBQUMsRUFBTyxDQUFDO0tBQzlEO0lBQUMsT0FBTyxLQUFLLEVBQUU7UUFDZCxPQUFPLENBQUMsS0FBSyxDQUFDLEtBQUssQ0FBQyxDQUFDO0tBQ3RCO0FBQ0gsQ0FBQzs7Ozs7O0FBRUQsTUFBTSxVQUFVLHlCQUF5QixDQUFDLFVBQWtCLEVBQUUsUUFBa0I7SUFDOUUsSUFBSTtRQUNGLE9BQU8sbUJBQUEsUUFBUSxDQUFDLEdBQUcsUUFBUSxFQUFFLEdBQUcsVUFBVSxxQkFBcUIsQ0FBQyxFQUF3QixDQUFDO0tBQzFGO0lBQUMsT0FBTyxLQUFLLEVBQUU7UUFDZCxPQUFPLENBQUMsS0FBSyxDQUFDLEtBQUssQ0FBQyxDQUFDO0tBQ3RCO0FBQ0gsQ0FBQzs7Ozs7OztBQUVELE1BQU0sVUFBVSxjQUFjLENBQUMsVUFBdUMsRUFBRSxPQUFZLEVBQUUsTUFBYTtJQUNqRyxJQUFJLFVBQVUsWUFBWSxRQUFRLEVBQUU7UUFDbEMsT0FBTyxVQUFVLENBQUMsS0FBSyxDQUFDLE9BQU8sRUFBRSxNQUFNLENBQUMsQ0FBQztLQUMxQztTQUFNO1FBQ0wsT0FBTyxVQUFVLENBQUMsQ0FBQyxDQUFDLElBQUksQ0FBQyxDQUFDLENBQUMsS0FBSyxDQUFDO0tBQ2xDO0FBQ0gsQ0FBQyIsInNvdXJjZXNDb250ZW50IjpbImV4cG9ydCBmdW5jdGlvbiBldmFsU3RyaW5nRXhwcmVzc2lvbihleHByZXNzaW9uOiBzdHJpbmcsIGFyZ05hbWVzOiBzdHJpbmdbXSkge1xuICB0cnkge1xuICAgIGlmIChleHByZXNzaW9uLmluZGV4T2YoJ3RoaXMuZmllbGQnKSAhPT0gLTEpIHtcbiAgICAgIGNvbnNvbGUud2FybihgTmd4Rm9ybWx5OiB1c2luZyAndGhpcy5maWVsZCcgaW4gZXhwcmVzc2lvblByb3BlcnRpZXMgaXMgZGVwcmVjYXRlZCBzaW5jZSB2NS4xLCB1c2UgJ2ZpZWxkJyBpbnN0ZWFkLmApO1xuICAgIH1cblxuICAgIHJldHVybiBGdW5jdGlvbiguLi5hcmdOYW1lcywgYHJldHVybiAke2V4cHJlc3Npb259O2ApIGFzIGFueTtcbiAgfSBjYXRjaCAoZXJyb3IpIHtcbiAgICBjb25zb2xlLmVycm9yKGVycm9yKTtcbiAgfVxufVxuXG5leHBvcnQgZnVuY3Rpb24gZXZhbEV4cHJlc3Npb25WYWx1ZVNldHRlcihleHByZXNzaW9uOiBzdHJpbmcsIGFyZ05hbWVzOiBzdHJpbmdbXSkge1xuICB0cnkge1xuICAgIHJldHVybiBGdW5jdGlvbiguLi5hcmdOYW1lcywgYCR7ZXhwcmVzc2lvbn0gPSBleHByZXNzaW9uVmFsdWU7YCkgYXMgKHZhbHVlOiBhbnkpID0+IHZvaWQ7XG4gIH0gY2F0Y2ggKGVycm9yKSB7XG4gICAgY29uc29sZS5lcnJvcihlcnJvcik7XG4gIH1cbn1cblxuZXhwb3J0IGZ1bmN0aW9uIGV2YWxFeHByZXNzaW9uKGV4cHJlc3Npb246IHN0cmluZyB8IEZ1bmN0aW9uIHwgYm9vbGVhbiwgdGhpc0FyZzogYW55LCBhcmdWYWw6IGFueVtdKTogYW55IHtcbiAgaWYgKGV4cHJlc3Npb24gaW5zdGFuY2VvZiBGdW5jdGlvbikge1xuICAgIHJldHVybiBleHByZXNzaW9uLmFwcGx5KHRoaXNBcmcsIGFyZ1ZhbCk7XG4gIH0gZWxzZSB7XG4gICAgcmV0dXJuIGV4cHJlc3Npb24gPyB0cnVlIDogZmFsc2U7XG4gIH1cbn1cbiJdfQ==