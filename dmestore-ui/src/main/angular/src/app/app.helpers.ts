import { FormControl, ValidationErrors } from '@angular/forms';

export const handleRes = (res, successOptions, faildOptions?) => {
  if (res.code === '200') {
    successOptions && successOptions.fn && successOptions.fn(res.data, res);
    return;
  } else {
    faildOptions && faildOptions.fn && faildOptions.fn(res);
    return;
  }
};

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
    console.log(
      '🚀 ~ file: app.helpers.ts ~ line 12 ~ string ~ stringData,defaultValue',
      stringData,
      defaultValue
    );
    console.error(error);
  }
  return target;
};

export const valueOrDefault = (value, defaultValue = '--') => {
  return value ? value : defaultValue;
};

/* 简单处理报错 */
export const handlerResponseErrorSimple = err => console.error('ERROR', err);

export async function getURL(vm) {
  return new Promise(resovle => vm.route.url.subscribe(resovle));
}

export async function getQueryParams(vm) {
  return new Promise(resovle => vm.route.queryParams.subscribe(resovle));
}

export const getColorByType = (prop, defaultValue = 'unset', type = 'a') => {
  const color_map = {
    a: {
      1: 'green',
      2: 'gold',
      3: 'red',
    },
  };
  const _colorMap = color_map[type];
  let target = _colorMap[prop];
  target = target ? target : defaultValue;
  return target;
};

export const getLabelByValue = (value, type, defaultValue = '') => {
  const LABEL_MAP = {
    alarm_state_map: {
      1: 'enum.status.normal',
      2: 'enum.status.warning',
      3: 'enum.status.alert',
    },
  };
  const _labelMap = LABEL_MAP[type];
  let target = _labelMap[value];
  target = target ? target : defaultValue;
  return target;
};

export const print = val => {
  return JSON.stringify(val, null, 2);
};

export function getQosCheckTipsTagInfo({
  /*  */
  qosTag,
  minBandwidthChoose,
  minBandwidth,
  maxBandwidthChoose,
  maxBandwidth,
  minIopsChoose,
  minIops,
  maxIopsChoose,
  maxIops,
  control_policyUpper,
  control_policyLower,
}) {
  const result = {
    bandwidthLimitErr: false,
    iopsLimitErr: false,
  };

  if (qosTag == 1) {
    if (minBandwidthChoose && maxBandwidthChoose) {
      // 带宽上限小于下限
      if (minBandwidth && maxBandwidth && Number(minBandwidth) > Number(maxBandwidth)) {
        result.bandwidthLimitErr = true;
      } else {
        result.bandwidthLimitErr = false;
      }
    } else {
      result.bandwidthLimitErr = false;
    }
    if (minIopsChoose && maxIopsChoose) {
      // iops上限小于下限
      if (minIops && maxIops && Number(minIops) > Number(maxIops)) {
        result.iopsLimitErr = true;
      } else {
        result.iopsLimitErr = false;
      }
    } else {
      result.iopsLimitErr = false;
    }

    /*v6 限制 v5 不限制*/
    if (maxIopsChoose && maxIops && Number(maxIops) < 100) {
      result.iopsLimitErr = true;
    }
  } else {
    result.iopsLimitErr = false;
    result.bandwidthLimitErr = false;
  }

  if (control_policyUpper == undefined) {
    result.iopsLimitErr = false;
    result.bandwidthLimitErr = false;
  }
  if (control_policyLower == undefined) {
    result.bandwidthLimitErr = false;
  }
  return result;
}

export const COLOR = {
  success: '#5eb715',
};

export const regExpCollection = {
  vmfsName: () =>
    /^[A-Za-z0-9\u4e00-\u9fa5\u3002\uff1f\uff01\uff0c\u3001\uff1b\uff1a\u201c\u201d\u2018\u2019\uff08\uff09\u300a\u300b\u3008\u3009\u3010\u3011\u300e\u300f\u300c\u300d\ufe43\ufe44\u3014\u3015\u2026\u2014\uff5e\ufe4f\uffe5\u00b7\-._]+$/g,
  nfsName: () => /^[0-9a-zA-Z_\-《：“”‘’，。；》——\u4e00-\u9fa5a\.]*$/,
  /* 中文字符 */
  chinise: () => /^[《：“”‘’，。；》——\u4e00-\u9fa5a]*$/,
  /* shareFsName */
  shareFsName: () => /^[A-Za-z0-9!\s!\\\"#&%\$'\(\)\*\+\-,·.:;<=>\?@\[\]\^_`\{\|\}~ ]{1,}$/,
  // shareFsName: new RegExp(`^[a-zA-Z0-9!\"#&%$'()*+-·.;<=>?@\[\]^_\`{|}~,:\s]*$`),
};

/**
 * 将字符串转JSON数组
 * @param obj
 */
export function getTypeOf(obj) {
  let object;
  if (typeof obj == 'string') {
    try {
      object = JSON.parse(obj);
    } catch (error) {
      console.log(error);
      object = obj;
    }
  } else {
    object = obj;
  }
  return object;
}

export function checkBoolean(item) {
  return typeof item === 'boolean';
}

export function checkString(item) {
  return typeof item === 'string';
}

export function isStringLengthByteOutRange(nameString, limit = 27, name = 'byte') {
  if (!nameString) return false;
  /* 字符 */
  if (name === 'letters') {
    return nameString.length > limit;
  }

  /* 字节 一个汉字三个字节 */
  let length = nameString.length;
  for (let index = 0; index < nameString.length - 1; index++) {
    const element = nameString.charAt(index);
    if (regExpCollection.chinise().test(element)) {
      length = length + 2;
    }
  }
  return length > limit;
}

export function formatCapacity(c: number, isGB: boolean) {
  let cNum;
  if (c < 1024) {
    cNum = isGB ? c.toFixed(3) + 'GB' : c.toFixed(3) + 'MB';
  } else if (c >= 1024 && c < 1048576) {
    cNum = isGB ? (c / 1024).toFixed(3) + 'TB' : (c / 1024).toFixed(3) + 'GB';
  } else if (c >= 1048576) {
    cNum = isGB ? (c / 1024 / 1024).toFixed(3) + 'PB' : (c / 1024 / 1024).toFixed(3) + 'TB';
  }
  return cNum;
}

export const transDateFormat = date => {
  let m: number | string = date.getMinutes();
  m = m < 10 ? `0${m}` : m;
  return `${date.getFullYear()}-${date.getMonth() + 1}-${date.getDate()} ${date.getHours()}:${m}`;
};

export const helper = {
  checkString,
  print,
  transDateFormat,
  /* 去重 */
  unique(arr: string[]) {
    return Array.from(new Set(arr));
  },
  valueFormObj(item) {
    for (const key of Object.keys(item)) {
      return item[key];
    }
  },
};

export const is = {
  array: item => Array.isArray(item),
  string: item => typeof item === 'string',
};
