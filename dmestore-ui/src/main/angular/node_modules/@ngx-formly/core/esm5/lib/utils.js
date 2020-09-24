/**
 * @fileoverview added by tsickle
 * @suppress {checkTypes,extraRequire,missingOverride,missingReturn,unusedPrivateMembers,uselessCode} checked by tsc
 */
import * as tslib_1 from "tslib";
import { isObservable } from 'rxjs';
import { AbstractControl } from '@angular/forms';
/**
 * @param {?} formId
 * @param {?} field
 * @param {?} index
 * @return {?}
 */
export function getFieldId(formId, field, index) {
    if (field.id)
        return field.id;
    /** @type {?} */
    var type = field.type;
    if (!type && field.template)
        type = 'template';
    return [formId, type, field.key, index].join('_');
}
/**
 * @param {?} field
 * @return {?}
 */
export function getKeyPath(field) {
    if (!field.key) {
        return [];
    }
    /* We store the keyPath in the field for performance reasons. This function will be called frequently. */
    if (!field._keyPath || field._keyPath.key !== field.key) {
        /** @type {?} */
        var path = [];
        if (typeof field.key === 'string') {
            /** @type {?} */
            var key = field.key.indexOf('[') === -1
                ? field.key
                : field.key.replace(/\[(\w+)\]/g, '.$1');
            path = key.indexOf('.') !== -1 ? key.split('.') : [key];
        }
        else if (Array.isArray(field.key)) {
            path = field.key.slice(0);
        }
        else {
            path = ["" + field.key];
        }
        field._keyPath = { key: field.key, path: path };
    }
    return field._keyPath.path.slice(0);
}
/** @type {?} */
export var FORMLY_VALIDATORS = ['required', 'pattern', 'minLength', 'maxLength', 'min', 'max'];
/**
 * @param {?} field
 * @param {?} value
 * @return {?}
 */
export function assignFieldValue(field, value) {
    /** @type {?} */
    var paths = getKeyPath(field);
    while (field.parent) {
        field = field.parent;
        paths = tslib_1.__spread(getKeyPath(field), paths);
    }
    if (value == null && field['autoClear'] && !field.formControl.parent) {
        /** @type {?} */
        var k = paths.pop();
        /** @type {?} */
        var m = paths.reduce((/**
         * @param {?} model
         * @param {?} path
         * @return {?}
         */
        function (model, path) { return model[path] || {}; }), field.parent.model);
        delete m[k];
        return;
    }
    assignModelValue(field.model, paths, value);
}
/**
 * @param {?} model
 * @param {?} paths
 * @param {?} value
 * @return {?}
 */
export function assignModelValue(model, paths, value) {
    for (var i = 0; i < (paths.length - 1); i++) {
        /** @type {?} */
        var path = paths[i];
        if (!model[path] || !isObject(model[path])) {
            model[path] = /^\d+$/.test(paths[i + 1]) ? [] : {};
        }
        model = model[path];
    }
    model[paths[paths.length - 1]] = clone(value);
}
/**
 * @param {?} field
 * @return {?}
 */
export function getFieldInitialValue(field) {
    var e_1, _a;
    /** @type {?} */
    var value = field.options['_initialModel'];
    /** @type {?} */
    var paths = getKeyPath(field);
    while (field.parent) {
        field = field.parent;
        paths = tslib_1.__spread(getKeyPath(field), paths);
    }
    try {
        for (var paths_1 = tslib_1.__values(paths), paths_1_1 = paths_1.next(); !paths_1_1.done; paths_1_1 = paths_1.next()) {
            var path = paths_1_1.value;
            if (!value) {
                return undefined;
            }
            value = value[path];
        }
    }
    catch (e_1_1) { e_1 = { error: e_1_1 }; }
    finally {
        try {
            if (paths_1_1 && !paths_1_1.done && (_a = paths_1.return)) _a.call(paths_1);
        }
        finally { if (e_1) throw e_1.error; }
    }
    return value;
}
/**
 * @param {?} field
 * @return {?}
 */
export function getFieldValue(field) {
    var e_2, _a;
    /** @type {?} */
    var model = field.parent.model;
    try {
        for (var _b = tslib_1.__values(getKeyPath(field)), _c = _b.next(); !_c.done; _c = _b.next()) {
            var path = _c.value;
            if (!model) {
                return model;
            }
            model = model[path];
        }
    }
    catch (e_2_1) { e_2 = { error: e_2_1 }; }
    finally {
        try {
            if (_c && !_c.done && (_a = _b.return)) _a.call(_b);
        }
        finally { if (e_2) throw e_2.error; }
    }
    return model;
}
/**
 * @param {?} dest
 * @param {...?} args
 * @return {?}
 */
export function reverseDeepMerge(dest) {
    var args = [];
    for (var _i = 1; _i < arguments.length; _i++) {
        args[_i - 1] = arguments[_i];
    }
    args.forEach((/**
     * @param {?} src
     * @return {?}
     */
    function (src) {
        for (var srcArg in src) {
            if (isNullOrUndefined(dest[srcArg]) || isBlankString(dest[srcArg])) {
                dest[srcArg] = clone(src[srcArg]);
            }
            else if (objAndSameType(dest[srcArg], src[srcArg])) {
                reverseDeepMerge(dest[srcArg], src[srcArg]);
            }
        }
    }));
    return dest;
}
/**
 * @param {?} value
 * @return {?}
 */
export function isNullOrUndefined(value) {
    return value === undefined || value === null;
}
/**
 * @param {?} value
 * @return {?}
 */
export function isUndefined(value) {
    return value === undefined;
}
/**
 * @param {?} value
 * @return {?}
 */
export function isBlankString(value) {
    return value === '';
}
/**
 * @param {?} value
 * @return {?}
 */
export function isFunction(value) {
    return typeof (value) === 'function';
}
/**
 * @param {?} obj1
 * @param {?} obj2
 * @return {?}
 */
export function objAndSameType(obj1, obj2) {
    return isObject(obj1) && isObject(obj2)
        && Object.getPrototypeOf(obj1) === Object.getPrototypeOf(obj2)
        && !(Array.isArray(obj1) || Array.isArray(obj2));
}
/**
 * @param {?} x
 * @return {?}
 */
export function isObject(x) {
    return x != null && typeof x === 'object';
}
/**
 * @param {?} obj
 * @return {?}
 */
export function isPromise(obj) {
    return !!obj && typeof obj.then === 'function';
}
/**
 * @param {?} value
 * @return {?}
 */
export function clone(value) {
    if (!isObject(value)
        || isObservable(value)
        || /* instanceof SafeHtmlImpl */ value.changingThisBreaksApplicationSecurity
        || ['RegExp', 'FileList', 'File', 'Blob'].indexOf(value.constructor.name) !== -1) {
        return value;
    }
    // https://github.com/moment/moment/blob/master/moment.js#L252
    if (value._isAMomentObject && isFunction(value.clone)) {
        return value.clone();
    }
    if (value instanceof AbstractControl) {
        return null;
    }
    if (value instanceof Date) {
        return new Date(value.getTime());
    }
    if (Array.isArray(value)) {
        return value.slice(0).map((/**
         * @param {?} v
         * @return {?}
         */
        function (v) { return clone(v); }));
    }
    // best way to clone a js object maybe
    // https://stackoverflow.com/questions/41474986/how-to-clone-a-javascript-es6-class-instance
    /** @type {?} */
    var proto = Object.getPrototypeOf(value);
    /** @type {?} */
    var c = Object.create(proto);
    c = Object.setPrototypeOf(c, proto);
    // need to make a deep copy so we dont use Object.assign
    // also Object.assign wont copy property descriptor exactly
    return Object.keys(value).reduce((/**
     * @param {?} newVal
     * @param {?} prop
     * @return {?}
     */
    function (newVal, prop) {
        /** @type {?} */
        var propDesc = Object.getOwnPropertyDescriptor(value, prop);
        if (propDesc.get) {
            Object.defineProperty(newVal, prop, propDesc);
        }
        else {
            newVal[prop] = clone(value[prop]);
        }
        return newVal;
    }), c);
}
/**
 * @param {?} field
 * @param {?} prop
 * @param {?} defaultValue
 * @return {?}
 */
export function defineHiddenProp(field, prop, defaultValue) {
    Object.defineProperty(field, prop, { enumerable: false, writable: true, configurable: true });
    field[prop] = defaultValue;
}
/**
 * @template T
 * @param {?} o
 * @param {?} prop
 * @param {?} setFn
 * @return {?}
 */
export function wrapProperty(o, prop, setFn) {
    if (!o._observers) {
        defineHiddenProp(o, '_observers', {});
    }
    if (!o._observers[prop]) {
        o._observers[prop] = [];
    }
    /** @type {?} */
    var fns = o._observers[prop];
    if (fns.indexOf(setFn) === -1) {
        fns.push(setFn);
        setFn({ currentValue: o[prop], firstChange: true });
        if (fns.length === 1) {
            defineHiddenProp(o, "___$" + prop, o[prop]);
            Object.defineProperty(o, prop, {
                configurable: true,
                get: (/**
                 * @return {?}
                 */
                function () { return o["___$" + prop]; }),
                set: (/**
                 * @param {?} currentValue
                 * @return {?}
                 */
                function (currentValue) {
                    if (currentValue !== o["___$" + prop]) {
                        /** @type {?} */
                        var previousValue_1 = o["___$" + prop];
                        o["___$" + prop] = currentValue;
                        fns.forEach((/**
                         * @param {?} changeFn
                         * @return {?}
                         */
                        function (changeFn) { return changeFn({ previousValue: previousValue_1, currentValue: currentValue, firstChange: false }); }));
                    }
                }),
            });
        }
    }
    return (/**
     * @return {?}
     */
    function () { return fns.splice(fns.indexOf(setFn), 1); });
}
/**
 * @param {?} form
 * @param {?} action
 * @return {?}
 */
export function reduceFormUpdateValidityCalls(form, action) {
    /** @type {?} */
    var updateValidity = form._updateTreeValidity.bind(form);
    /** @type {?} */
    var updateValidityArgs = { called: false, emitEvent: false };
    form._updateTreeValidity = (/**
     * @param {?=} __0
     * @return {?}
     */
    function (_a) {
        var emitEvent = (_a === void 0 ? { emitEvent: true } : _a).emitEvent;
        return updateValidityArgs = { called: true, emitEvent: emitEvent || updateValidityArgs.emitEvent };
    });
    action();
    updateValidityArgs.called && updateValidity({ emitEvent: updateValidityArgs.emitEvent });
    form._updateTreeValidity = updateValidity;
}
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoidXRpbHMuanMiLCJzb3VyY2VSb290Ijoibmc6Ly9Abmd4LWZvcm1seS9jb3JlLyIsInNvdXJjZXMiOlsibGliL3V0aWxzLnRzIl0sIm5hbWVzIjpbXSwibWFwcGluZ3MiOiI7Ozs7O0FBQ0EsT0FBTyxFQUFFLFlBQVksRUFBRSxNQUFNLE1BQU0sQ0FBQztBQUNwQyxPQUFPLEVBQUUsZUFBZSxFQUFFLE1BQU0sZ0JBQWdCLENBQUM7Ozs7Ozs7QUFHakQsTUFBTSxVQUFVLFVBQVUsQ0FBQyxNQUFjLEVBQUUsS0FBd0IsRUFBRSxLQUFvQjtJQUN2RixJQUFJLEtBQUssQ0FBQyxFQUFFO1FBQUUsT0FBTyxLQUFLLENBQUMsRUFBRSxDQUFDOztRQUMxQixJQUFJLEdBQUcsS0FBSyxDQUFDLElBQUk7SUFDckIsSUFBSSxDQUFDLElBQUksSUFBSSxLQUFLLENBQUMsUUFBUTtRQUFFLElBQUksR0FBRyxVQUFVLENBQUM7SUFDL0MsT0FBTyxDQUFDLE1BQU0sRUFBRSxJQUFJLEVBQUUsS0FBSyxDQUFDLEdBQUcsRUFBRSxLQUFLLENBQUMsQ0FBQyxJQUFJLENBQUMsR0FBRyxDQUFDLENBQUM7QUFDcEQsQ0FBQzs7Ozs7QUFFRCxNQUFNLFVBQVUsVUFBVSxDQUFDLEtBQTZCO0lBQ3RELElBQUksQ0FBQyxLQUFLLENBQUMsR0FBRyxFQUFFO1FBQ2QsT0FBTyxFQUFFLENBQUM7S0FDWDtJQUVELHlHQUF5RztJQUN6RyxJQUFJLENBQUMsS0FBSyxDQUFDLFFBQVEsSUFBSSxLQUFLLENBQUMsUUFBUSxDQUFDLEdBQUcsS0FBSyxLQUFLLENBQUMsR0FBRyxFQUFFOztZQUNuRCxJQUFJLEdBQWEsRUFBRTtRQUN2QixJQUFJLE9BQU8sS0FBSyxDQUFDLEdBQUcsS0FBSyxRQUFRLEVBQUU7O2dCQUMzQixHQUFHLEdBQUcsS0FBSyxDQUFDLEdBQUcsQ0FBQyxPQUFPLENBQUMsR0FBRyxDQUFDLEtBQUssQ0FBQyxDQUFDO2dCQUN2QyxDQUFDLENBQUMsS0FBSyxDQUFDLEdBQUc7Z0JBQ1gs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OlsiaW1wb3J0IHsgRm9ybWx5RmllbGRDb25maWcgfSBmcm9tICcuL2NvcmUnO1xuaW1wb3J0IHsgaXNPYnNlcnZhYmxlIH0gZnJvbSAncnhqcyc7XG5pbXBvcnQgeyBBYnN0cmFjdENvbnRyb2wgfSBmcm9tICdAYW5ndWxhci9mb3Jtcyc7XG5pbXBvcnQgeyBGb3JtbHlGaWVsZENvbmZpZ0NhY2hlIH0gZnJvbSAnLi9jb21wb25lbnRzL2Zvcm1seS5maWVsZC5jb25maWcnO1xuXG5leHBvcnQgZnVuY3Rpb24gZ2V0RmllbGRJZChmb3JtSWQ6IHN0cmluZywgZmllbGQ6IEZvcm1seUZpZWxkQ29uZmlnLCBpbmRleDogc3RyaW5nfG51bWJlcikge1xuICBpZiAoZmllbGQuaWQpIHJldHVybiBmaWVsZC5pZDtcbiAgbGV0IHR5cGUgPSBmaWVsZC50eXBlO1xuICBpZiAoIXR5cGUgJiYgZmllbGQudGVtcGxhdGUpIHR5cGUgPSAndGVtcGxhdGUnO1xuICByZXR1cm4gW2Zvcm1JZCwgdHlwZSwgZmllbGQua2V5LCBpbmRleF0uam9pbignXycpO1xufVxuXG5leHBvcnQgZnVuY3Rpb24gZ2V0S2V5UGF0aChmaWVsZDogRm9ybWx5RmllbGRDb25maWdDYWNoZSk6IHN0cmluZ1tdIHtcbiAgaWYgKCFmaWVsZC5rZXkpIHtcbiAgICByZXR1cm4gW107XG4gIH1cblxuICAvKiBXZSBzdG9yZSB0aGUga2V5UGF0aCBpbiB0aGUgZmllbGQgZm9yIHBlcmZvcm1hbmNlIHJlYXNvbnMuIFRoaXMgZnVuY3Rpb24gd2lsbCBiZSBjYWxsZWQgZnJlcXVlbnRseS4gKi9cbiAgaWYgKCFmaWVsZC5fa2V5UGF0aCB8fCBmaWVsZC5fa2V5UGF0aC5rZXkgIT09IGZpZWxkLmtleSkge1xuICAgIGxldCBwYXRoOiBzdHJpbmdbXSA9IFtdO1xuICAgIGlmICh0eXBlb2YgZmllbGQua2V5ID09PSAnc3RyaW5nJykge1xuICAgICAgY29uc3Qga2V5ID0gZmllbGQua2V5LmluZGV4T2YoJ1snKSA9PT0gLTFcbiAgICAgICAgPyBmaWVsZC5rZXlcbiAgICAgICAgOiBmaWVsZC5rZXkucmVwbGFjZSgvXFxbKFxcdyspXFxdL2csICcuJDEnKTtcbiAgICAgIHBhdGggPSBrZXkuaW5kZXhPZignLicpICE9PSAtMSA/IGtleS5zcGxpdCgnLicpIDogW2tleV07XG4gICAgfSBlbHNlIGlmIChBcnJheS5pc0FycmF5KGZpZWxkLmtleSkpIHtcbiAgICAgIHBhdGggPSBmaWVsZC5rZXkuc2xpY2UoMCk7XG4gICAgfSBlbHNlIHtcbiAgICAgIHBhdGggPSBbYCR7ZmllbGQua2V5fWBdO1xuICAgIH1cblxuICAgIGZpZWxkLl9rZXlQYXRoID0geyBrZXk6IGZpZWxkLmtleSwgcGF0aCB9O1xuICB9XG5cbiAgcmV0dXJuIGZpZWxkLl9rZXlQYXRoLnBhdGguc2xpY2UoMCk7XG59XG5cbmV4cG9ydCBjb25zdCBGT1JNTFlfVkFMSURBVE9SUyA9IFsncmVxdWlyZWQnLCAncGF0dGVybicsICdtaW5MZW5ndGgnLCAnbWF4TGVuZ3RoJywgJ21pbicsICdtYXgnXTtcblxuZXhwb3J0IGZ1bmN0aW9uIGFzc2lnbkZpZWxkVmFsdWUoZmllbGQ6IEZvcm1seUZpZWxkQ29uZmlnQ2FjaGUsIHZhbHVlOiBhbnkpIHtcbiAgbGV0IHBhdGhzID0gZ2V0S2V5UGF0aChmaWVsZCk7XG4gIHdoaWxlIChmaWVsZC5wYXJlbnQpIHtcbiAgICBmaWVsZCA9IGZpZWxkLnBhcmVudDtcbiAgICBwYXRocyA9IFsuLi5nZXRLZXlQYXRoKGZpZWxkKSwgLi4ucGF0aHNdO1xuICB9XG5cbiAgaWYgKHZhbHVlID09IG51bGwgJiYgZmllbGRbJ2F1dG9DbGVhciddICYmICFmaWVsZC5mb3JtQ29udHJvbC5wYXJlbnQpIHtcbiAgICBjb25zdCBrID0gcGF0aHMucG9wKCk7XG4gICAgY29uc3QgbSA9IHBhdGhzLnJlZHVjZSgobW9kZWwsIHBhdGgpID0+IG1vZGVsW3BhdGhdIHx8IHt9LCBmaWVsZC5wYXJlbnQubW9kZWwpO1xuICAgIGRlbGV0ZSBtW2tdO1xuICAgIHJldHVybjtcbiAgfVxuXG4gIGFzc2lnbk1vZGVsVmFsdWUoZmllbGQubW9kZWwsIHBhdGhzLCB2YWx1ZSk7XG59XG5cbmV4cG9ydCBmdW5jdGlvbiBhc3NpZ25Nb2RlbFZhbHVlKG1vZGVsOiBhbnksIHBhdGhzOiBzdHJpbmdbXSwgdmFsdWU6IGFueSkge1xuICBmb3IgKGxldCBpID0gMDsgaSA8IChwYXRocy5sZW5ndGggLSAxKTsgaSsrKSB7XG4gICAgY29uc3QgcGF0aCA9IHBhdGhzW2ldO1xuICAgIGlmICghbW9kZWxbcGF0aF0gfHwgIWlzT2JqZWN0KG1vZGVsW3BhdGhdKSkge1xuICAgICAgbW9kZWxbcGF0aF0gPSAvXlxcZCskLy50ZXN0KHBhdGhzW2kgKyAxXSkgPyBbXSA6IHt9O1xuICAgIH1cblxuICAgIG1vZGVsID0gbW9kZWxbcGF0aF07XG4gIH1cblxuICBtb2RlbFtwYXRoc1twYXRocy5sZW5ndGggLSAxXV0gPSBjbG9uZSh2YWx1ZSk7XG59XG5cbmV4cG9ydCBmdW5jdGlvbiBnZXRGaWVsZEluaXRpYWxWYWx1ZShmaWVsZDogRm9ybWx5RmllbGRDb25maWcpIHtcbiAgbGV0IHZhbHVlID0gZmllbGQub3B0aW9uc1snX2luaXRpYWxNb2RlbCddO1xuICBsZXQgcGF0aHMgPSBnZXRLZXlQYXRoKGZpZWxkKTtcbiAgd2hpbGUgKGZpZWxkLnBhcmVudCkge1xuICAgIGZpZWxkID0gZmllbGQucGFyZW50O1xuICAgIHBhdGhzID0gWy4uLmdldEtleVBhdGgoZmllbGQpLCAuLi5wYXRoc107XG4gIH1cblxuICBmb3IgKGNvbnN0IHBhdGggb2YgcGF0aHMpIHtcbiAgICBpZiAoIXZhbHVlKSB7XG4gICAgICByZXR1cm4gdW5kZWZpbmVkO1xuICAgIH1cbiAgICB2YWx1ZSA9IHZhbHVlW3BhdGhdO1xuICB9XG5cbiAgcmV0dXJuIHZhbHVlO1xufVxuXG5leHBvcnQgZnVuY3Rpb24gZ2V0RmllbGRWYWx1ZShmaWVsZDogRm9ybWx5RmllbGRDb25maWcpOiBhbnkge1xuICBsZXQgbW9kZWwgPSBmaWVsZC5wYXJlbnQubW9kZWw7XG4gIGZvciAoY29uc3QgcGF0aCBvZiBnZXRLZXlQYXRoKGZpZWxkKSkge1xuICAgIGlmICghbW9kZWwpIHtcbiAgICAgIHJldHVybiBtb2RlbDtcbiAgICB9XG4gICAgbW9kZWwgPSBtb2RlbFtwYXRoXTtcbiAgfVxuXG4gIHJldHVybiBtb2RlbDtcbn1cblxuZXhwb3J0IGZ1bmN0aW9uIHJldmVyc2VEZWVwTWVyZ2UoZGVzdDogYW55LCAuLi5hcmdzOiBhbnlbXSkge1xuICBhcmdzLmZvckVhY2goc3JjID0+IHtcbiAgICBmb3IgKGxldCBzcmNBcmcgaW4gc3JjKSB7XG4gICAgICBpZiAoaXNOdWxsT3JVbmRlZmluZWQoZGVzdFtzcmNBcmddKSB8fCBpc0JsYW5rU3RyaW5nKGRlc3Rbc3JjQXJnXSkpIHtcbiAgICAgICAgZGVzdFtzcmNBcmddID0gY2xvbmUoc3JjW3NyY0FyZ10pO1xuICAgICAgfSBlbHNlIGlmIChvYmpBbmRTYW1lVHlwZShkZXN0W3NyY0FyZ10sIHNyY1tzcmNBcmddKSkge1xuICAgICAgICByZXZlcnNlRGVlcE1lcmdlKGRlc3Rbc3JjQXJnXSwgc3JjW3NyY0FyZ10pO1xuICAgICAgfVxuICAgIH1cbiAgfSk7XG4gIHJldHVybiBkZXN0O1xufVxuXG5leHBvcnQgZnVuY3Rpb24gaXNOdWxsT3JVbmRlZmluZWQodmFsdWU6IGFueSkge1xuICByZXR1cm4gdmFsdWUgPT09IHVuZGVmaW5lZCB8fCB2YWx1ZSA9PT0gbnVsbDtcbn1cblxuZXhwb3J0IGZ1bmN0aW9uIGlzVW5kZWZpbmVkKHZhbHVlOiBhbnkpIHtcbiAgcmV0dXJuIHZhbHVlID09PSB1bmRlZmluZWQ7XG59XG5cbmV4cG9ydCBmdW5jdGlvbiBpc0JsYW5rU3RyaW5nKHZhbHVlOiBhbnkpIHtcbiAgcmV0dXJuIHZhbHVlID09PSAnJztcbn1cblxuZXhwb3J0IGZ1bmN0aW9uIGlzRnVuY3Rpb24odmFsdWU6IGFueSkge1xuICByZXR1cm4gdHlwZW9mKHZhbHVlKSA9PT0gJ2Z1bmN0aW9uJztcbn1cblxuZXhwb3J0IGZ1bmN0aW9uIG9iakFuZFNhbWVUeXBlKG9iajE6IGFueSwgb2JqMjogYW55KSB7XG4gIHJldHVybiBpc09iamVjdChvYmoxKSAmJiBpc09iamVjdChvYmoyKVxuICAgICYmIE9iamVjdC5nZXRQcm90b3R5cGVPZihvYmoxKSA9PT0gT2JqZWN0LmdldFByb3RvdHlwZU9mKG9iajIpXG4gICAgJiYgIShBcnJheS5pc0FycmF5KG9iajEpIHx8IEFycmF5LmlzQXJyYXkob2JqMikpO1xufVxuXG5leHBvcnQgZnVuY3Rpb24gaXNPYmplY3QoeDogYW55KSB7XG4gIHJldHVybiB4ICE9IG51bGwgJiYgdHlwZW9mIHggPT09ICdvYmplY3QnO1xufVxuXG5leHBvcnQgZnVuY3Rpb24gaXNQcm9taXNlKG9iajogYW55KTogb2JqIGlzIFByb21pc2U8YW55PiB7XG4gIHJldHVybiAhIW9iaiAmJiB0eXBlb2Ygb2JqLnRoZW4gPT09ICdmdW5jdGlvbic7XG59XG5cbmV4cG9ydCBmdW5jdGlvbiBjbG9uZSh2YWx1ZTogYW55KTogYW55IHtcbiAgaWYgKFxuICAgICFpc09iamVjdCh2YWx1ZSlcbiAgICB8fCBpc09ic2VydmFibGUodmFsdWUpXG4gICAgfHwgLyogaW5zdGFuY2VvZiBTYWZlSHRtbEltcGwgKi8gdmFsdWUuY2hhbmdpbmdUaGlzQnJlYWtzQXBwbGljYXRpb25TZWN1cml0eVxuICAgIHx8IFsnUmVnRXhwJywgJ0ZpbGVMaXN0JywgJ0ZpbGUnLCAnQmxvYiddLmluZGV4T2YodmFsdWUuY29uc3RydWN0b3IubmFtZSkgIT09IC0xXG4gICkge1xuICAgIHJldHVybiB2YWx1ZTtcbiAgfVxuXG4gIC8vIGh0dHBzOi8vZ2l0aHViLmNvbS9tb21lbnQvbW9tZW50L2Jsb2IvbWFzdGVyL21vbWVudC5qcyNMMjUyXG4gIGlmICh2YWx1ZS5faXNBTW9tZW50T2JqZWN0ICYmIGlzRnVuY3Rpb24odmFsdWUuY2xvbmUpKSB7XG4gICAgcmV0dXJuIHZhbHVlLmNsb25lKCk7XG4gIH1cblxuICBpZiAodmFsdWUgaW5zdGFuY2VvZiBBYnN0cmFjdENvbnRyb2wpIHtcbiAgICByZXR1cm4gbnVsbDtcbiAgfVxuXG4gIGlmICh2YWx1ZSBpbnN0YW5jZW9mIERhdGUpIHtcbiAgICByZXR1cm4gbmV3IERhdGUodmFsdWUuZ2V0VGltZSgpKTtcbiAgfVxuXG4gIGlmIChBcnJheS5pc0FycmF5KHZhbHVlKSkge1xuICAgIHJldHVybiB2YWx1ZS5zbGljZSgwKS5tYXAodiA9PiBjbG9uZSh2KSk7XG4gIH1cblxuICAvLyBiZXN0IHdheSB0byBjbG9uZSBhIGpzIG9iamVjdCBtYXliZVxuICAvLyBodHRwczovL3N0YWNrb3ZlcmZsb3cuY29tL3F1ZXN0aW9ucy80MTQ3NDk4Ni9ob3ctdG8tY2xvbmUtYS1qYXZhc2NyaXB0LWVzNi1jbGFzcy1pbnN0YW5jZVxuICBjb25zdCBwcm90byA9IE9iamVjdC5nZXRQcm90b3R5cGVPZih2YWx1ZSk7XG4gIGxldCBjID0gT2JqZWN0LmNyZWF0ZShwcm90byk7XG4gIGMgPSBPYmplY3Quc2V0UHJvdG90eXBlT2YoYywgcHJvdG8pO1xuICAvLyBuZWVkIHRvIG1ha2UgYSBkZWVwIGNvcHkgc28gd2UgZG9udCB1c2UgT2JqZWN0LmFzc2lnblxuICAvLyBhbHNvIE9iamVjdC5hc3NpZ24gd29udCBjb3B5IHByb3BlcnR5IGRlc2NyaXB0b3IgZXhhY3RseVxuICByZXR1cm4gT2JqZWN0LmtleXModmFsdWUpLnJlZHVjZSgobmV3VmFsLCBwcm9wKSA9PiB7XG4gICAgY29uc3QgcHJvcERlc2MgPSBPYmplY3QuZ2V0T3duUHJvcGVydHlEZXNjcmlwdG9yKHZhbHVlLCBwcm9wKTtcbiAgICBpZiAocHJvcERlc2MuZ2V0KSB7XG4gICAgICBPYmplY3QuZGVmaW5lUHJvcGVydHkobmV3VmFsLCBwcm9wLCBwcm9wRGVzYyk7XG4gICAgfSBlbHNlIHtcbiAgICAgIG5ld1ZhbFtwcm9wXSA9IGNsb25lKHZhbHVlW3Byb3BdKTtcbiAgICB9XG5cbiAgICByZXR1cm4gbmV3VmFsO1xuICB9LCBjKTtcbn1cblxuZXhwb3J0IGZ1bmN0aW9uIGRlZmluZUhpZGRlblByb3AoZmllbGQ6IGFueSwgcHJvcDogc3RyaW5nLCBkZWZhdWx0VmFsdWU6IGFueSkge1xuICBPYmplY3QuZGVmaW5lUHJvcGVydHkoZmllbGQsIHByb3AsIHsgZW51bWVyYWJsZTogZmFsc2UsIHdyaXRhYmxlOiB0cnVlLCBjb25maWd1cmFibGU6IHRydWUgfSk7XG4gIGZpZWxkW3Byb3BdID0gZGVmYXVsdFZhbHVlO1xufVxuXG5leHBvcnQgZnVuY3Rpb24gd3JhcFByb3BlcnR5PFQgPSBhbnk+KFxuICBvOiBhbnksXG4gIHByb3A6IHN0cmluZyxcbiAgc2V0Rm46IChjaGFuZ2U6IHtjdXJyZW50VmFsdWU6IFQsIHByZXZpb3VzVmFsdWU/OiBULCBmaXJzdENoYW5nZTogYm9vbGVhbn0pID0+IHZvaWQsXG4pIHtcbiAgaWYgKCFvLl9vYnNlcnZlcnMpIHtcbiAgICBkZWZpbmVIaWRkZW5Qcm9wKG8sICdfb2JzZXJ2ZXJzJywge30pO1xuICB9XG5cbiAgaWYgKCFvLl9vYnNlcnZlcnNbcHJvcF0pIHtcbiAgICBvLl9vYnNlcnZlcnNbcHJvcF0gPSBbXTtcbiAgfVxuXG4gIGxldCBmbnM6IHR5cGVvZiBzZXRGbltdID0gby5fb2JzZXJ2ZXJzW3Byb3BdO1xuICBpZiAoZm5zLmluZGV4T2Yoc2V0Rm4pID09PSAtMSkge1xuICAgIGZucy5wdXNoKHNldEZuKTtcbiAgICBzZXRGbih7IGN1cnJlbnRWYWx1ZTogb1twcm9wXSwgZmlyc3RDaGFuZ2U6IHRydWUgfSk7XG4gICAgaWYgKGZucy5sZW5ndGggPT09IDEpIHtcbiAgICAgIGRlZmluZUhpZGRlblByb3AobywgYF9fXyQke3Byb3B9YCwgb1twcm9wXSk7XG4gICAgICBPYmplY3QuZGVmaW5lUHJvcGVydHkobywgcHJvcCwge1xuICAgICAgICBjb25maWd1cmFibGU6IHRydWUsXG4gICAgICAgIGdldDogKCkgPT4gb1tgX19fJCR7cHJvcH1gXSxcbiAgICAgICAgc2V0OiBjdXJyZW50VmFsdWUgPT4ge1xuICAgICAgICAgIGlmIChjdXJyZW50VmFsdWUgIT09IG9bYF9fXyQke3Byb3B9YF0pIHtcbiAgICAgICAgICAgIGNvbnN0IHByZXZpb3VzVmFsdWUgPSBvW2BfX18kJHtwcm9wfWBdO1xuICAgICAgICAgICAgb1tgX19fJCR7cHJvcH1gXSA9IGN1cnJlbnRWYWx1ZTtcbiAgICAgICAgICAgIGZucy5mb3JFYWNoKGNoYW5nZUZuID0+IGNoYW5nZUZuKHsgcHJldmlvdXNWYWx1ZSwgY3VycmVudFZhbHVlLCBmaXJzdENoYW5nZTogZmFsc2UgfSkpO1xuICAgICAgICAgIH1cbiAgICAgICAgfSxcbiAgICAgIH0pO1xuICAgIH1cbiAgfVxuXG4gIHJldHVybiAoKSA9PiBmbnMuc3BsaWNlKGZucy5pbmRleE9mKHNldEZuKSwgMSk7XG59XG5cbmV4cG9ydCBmdW5jdGlvbiByZWR1Y2VGb3JtVXBkYXRlVmFsaWRpdHlDYWxscyhmb3JtOiBhbnksIGFjdGlvbjogRnVuY3Rpb24pIHtcbiAgY29uc3QgdXBkYXRlVmFsaWRpdHkgPSBmb3JtLl91cGRhdGVUcmVlVmFsaWRpdHkuYmluZChmb3JtKTtcblxuICBsZXQgdXBkYXRlVmFsaWRpdHlBcmdzID0geyBjYWxsZWQ6IGZhbHNlLCBlbWl0RXZlbnQ6IGZhbHNlIH07XG4gIGZvcm0uX3VwZGF0ZVRyZWVWYWxpZGl0eSA9ICh7IGVtaXRFdmVudCB9ID0geyBlbWl0RXZlbnQ6IHRydWUgfSkgPT4gdXBkYXRlVmFsaWRpdHlBcmdzID0geyBjYWxsZWQ6IHRydWUsIGVtaXRFdmVudDogZW1pdEV2ZW50IHx8IHVwZGF0ZVZhbGlkaXR5QXJncy5lbWl0RXZlbnQgfTtcbiAgYWN0aW9uKCk7XG5cbiAgdXBkYXRlVmFsaWRpdHlBcmdzLmNhbGxlZCAmJiB1cGRhdGVWYWxpZGl0eSh7IGVtaXRFdmVudDogdXBkYXRlVmFsaWRpdHlBcmdzLmVtaXRFdmVudCB9KTtcbiAgZm9ybS5fdXBkYXRlVHJlZVZhbGlkaXR5ID0gdXBkYXRlVmFsaWRpdHk7XG59XG4iXX0=