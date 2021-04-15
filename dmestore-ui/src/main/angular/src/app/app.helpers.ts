export const handleRes = (res, successOptions, faildOptions?) => {
  if (res.code === "200") {
    successOptions && successOptions.fn && successOptions.fn(res.data, res);
    return;
  } else {
    faildOptions && faildOptions.fn && faildOptions.fn(res);
    return;
  }
}

/**
 * @Description json字符串转对象，
 * @date 2021-04-13
 * @param {any} stringData
 * @param {any} defaultValue 默认值
 * @returns {any}
 */
export const stringParseObj = (stringData, defaultValue) => {
  let target = defaultValue;
  try {
    target = JSON.parse(stringData);
  } catch (error) {
    console.log("🚀 ~ file: app.helpers.ts ~ line 12 ~ string ~ stringData,defaultValue", stringData, defaultValue);
    console.error(error)
  }
  return target
}

export const valueOrDefault = (value, defaultValue = "--") => {
  return value ? value : defaultValue;
}

/* 简单处理报错 */
export const handlerResponseErrorSimple = err => console.error('ERROR', err);

export async function getURL(vm){
  return new Promise(resovle => vm.route.url.subscribe(resovle))
}
export async function getQueryParams(vm){
  return new Promise(resovle => vm.route.queryParams.subscribe(resovle))
}