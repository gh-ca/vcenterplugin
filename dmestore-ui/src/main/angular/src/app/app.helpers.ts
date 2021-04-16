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

export const getColorByType = (prop, defaultValue = 'unset', type = 'a') => {
  const color_map = {
    a:{
      1: 'green',
      2: 'gold',
      3: 'red'
    }
  }
  const _colorMap = color_map[type];
  let target = _colorMap[prop];
  target = target ? target:defaultValue;
  return target;
}

export const getLabelByValue = (value, type, defaultValue = "") => {
  const LABEL_MAP = {
    alarm_state_map : {
      1: 'enum.status.normal',
      2: 'enum.status.warning',
      3: 'enum.status.alert',
    }
  }
  const _labelMap = LABEL_MAP[type];
  let target = _labelMap[value];
  target = target ? target:defaultValue;
  return target;
}