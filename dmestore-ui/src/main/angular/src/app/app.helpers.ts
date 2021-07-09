import { AbstractControl } from '@angular/forms';
import { ClrSelectedState } from '@clr/angular';
import { ValidatorFn } from '@angular/forms';
import { FormControl, ValidationErrors } from '@angular/forms';
import { getLodash } from './shared/lib';
import { host } from '@angular-devkit/build-angular/src/test-utils';

export const _ = getLodash();

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
    /* v6 上下限都要大于100 */
    if (minIopsChoose && minIops && Number(minIops) < 100) {
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
  nfsName: () => /^[0-9a-zA-Z_\-《：“”‘’，。；》——\u4e00-\u9fa5\.]*$/,
  /* 中文字符 */
  chinise: () => /^[《：“”‘’，。；》——\u4e00-\u9fa5]*$/,
  /* shareFsName */
  shareFsName: () => /^[A-Za-z0-9!\s!\\\"#&%\$'\(\)\*\+\-,·.:;<=>\?@\[\]\^_`\{\|\}~ ]{1,}$/,
  // shareFsName: new RegExp(`^[a-zA-Z0-9!\"#&%$'()*+-·.;<=>?@\[\]^_\`{|}~,:\s]*$`),
  integer: () => /^[1-9]\d*$/,
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
  let length = Number(nameString.length);
  for (let index = 0; index < nameString.length; index++) {
    const element = nameString.charAt(index);
    if (regExpCollection.chinise().test(element)) {
      length = length + 2;
      console.log(
        '🚀 ~ file: app.helpers.ts ~ line 197 ~ isStringLengthByteOutRange ~ length',
        length
      );
    }
  }
  return length > limit;
}

export function formatCapacity(c: number, isGB: boolean) {
  let cNum;
  try {
    if (c < 1024) {
      cNum = isGB ? c.toFixed(3) + 'GB' : c.toFixed(3) + 'MB';
    } else if (c >= 1024 && c < 1048576) {
      cNum = isGB ? (c / 1024).toFixed(3) + 'TB' : (c / 1024).toFixed(3) + 'GB';
    } else if (c >= 1048576) {
      cNum = isGB ? (c / 1024 / 1024).toFixed(3) + 'PB' : (c / 1024 / 1024).toFixed(3) + 'TB';
    }
  } catch (error) {
    console.error(error);
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

export function CustomValidatorFaild(checkFn: any): ValidatorFn {
  return (control: AbstractControl): { [key: string]: any } | null => {
    return checkFn(control.value) ? { custom: { value: control.value } } : null;
  };
}

export type VMFS_CLUSTER_NODE = {
  clusterId: string;
  clusterName: string;
  children?: any[];
  selected?: number;
};

/* vmfs 挂载专用 */
export function vmfsGetSelectedFromTree(clusterArray: VMFS_CLUSTER_NODE[], mountType = '') {
  let result = [];
  let selectedCluster: any = false;
  /* 一旦选择一个集群，只能该集权 */
  /* 一旦选择一个主机，只能该主机所在集群 */
  for (const clusterNode of clusterArray) {
    let _node: any = clusterNode;

    if (String(clusterNode.selected) === String(ClrSelectedState.SELECTED)) {
      /* 第一层本身是host */
      if ((clusterNode as any).deviceType === 'host') {
        _node = _.omit(clusterNode, ['children']);
        result.push(_node);
      } else if (mountType === 'host') {
        /* 创建方式是host */
        _node = clusterNode;
      } else {
        /* 创建方式是集群 */
        /* 集群下有主机是false，以主机方式 */
        const someFalse = _.some(
          clusterNode?.children,
          hostNode => String(hostNode.flag) === 'false'
        );
        if (someFalse) {
          _node = clusterNode;
        } else {
          _node = _.omit(clusterNode, ['children']);
          result.push(_node);
        }
      }
    }

    /* 通过控制children来限制是否传主机 */
    if (_node.children && _node.children.length > 0) {
      for (const hostNode of _node.children) {
        if (String(hostNode.selected) === String(ClrSelectedState.SELECTED)) {
          if (hostNode.flag !== false) {
            const _node = _.omit(hostNode, ['children']);
            if (!selectedCluster) {
              selectedCluster = clusterNode;
            }
            result.push(_node);
          }
        }
      }
    }
  }
  return result;
}

/**
 * @Description vmfs 集群 主机 树 第一层是集群 第二层是主机
 * 选了集群只传集群，
 *
 * @date 2021-05-12
 * @param {any} clusterArray:VMFS_CLUSTER_NODE[]
 * @returns {any}
 */
export function getSelectedFromTree(
  clusterArray: VMFS_CLUSTER_NODE[],
  resType = '',
  mountType = ''
) {
  let result = [];
  let selectedCluster: any = false;
  let selectedHost: any = false;
  /* 一旦选择单主机，只能单主机 */
  /* 一旦选择一个集群，只能该集群 */
  /* 一旦选择一个主机，只能该主机所在集群 */
  for (const clusterNode of clusterArray) {
    if (mountType === 'host') {
      if (String(clusterNode.selected) === String(ClrSelectedState.SELECTED)) {
        if ((clusterNode as any).deviceType === 'host') {
          result.push(_.omit(clusterNode, ['children']));
        }

        if (!selectedCluster) {
          selectedCluster = clusterNode;
        }
      }
    } else {
      if (String(clusterNode.selected) === String(ClrSelectedState.SELECTED)) {
        const _node = _.omit(clusterNode, ['children']);
        if (_node.deviceType === 'host') {
          selectedHost = _node;
        }
        // _node['deviceType'] = 'cluster';
        result.push(_node);
        // console.log(result)
        if (!selectedCluster) {
          selectedCluster = clusterNode;
        }
        continue;
      }
    }

    if (clusterNode.children && clusterNode.children.length > 0) {
      for (const hostNode of clusterNode.children) {
        if (String(hostNode.selected) === String(ClrSelectedState.SELECTED)) {
          const _node = _.omit(hostNode, ['children']);
          // _node['deviceType'] = 'host';
          if (!selectedCluster) {
            selectedCluster = clusterNode;
          }
          result.push(_node);
          // console.log(result)
        }
      }
    }
  }

  const setDisabled = setDisabledTrue => {
    /* 需要设置disabled，部分置灰 */
    for (const clusterNode of clusterArray) {

      const setSingleHostDisabled = () => {
        if ((clusterNode as any).deviceType === 'host') {
          (clusterNode as any).isDisabled = false;
        } else {
          (clusterNode as any).isDisabled = true;
        }
      };

      const setOtherDisabled = () => {
        (clusterNode as any).isDisabled = true;
        if (clusterNode.children && clusterNode.children.length > 0) {
          for (const hostNode of clusterNode.children) {
            (hostNode as any).isDisabled = true;
          }
        }
      };

      const setEnable = () => {
        (clusterNode as any).isDisabled = false;
        if (clusterNode.children && clusterNode.children.length > 0) {
          for (const hostNode of clusterNode.children) {
            (hostNode as any).isDisabled = false;
          }
        }
      };

      const setAllEnable = () => {
        (clusterNode as any).isDisabled = false;
        if (clusterNode.children && clusterNode.children.length > 0) {
          for (const hostNode of clusterNode.children) {
            (hostNode as any).isDisabled = false;
          }
        }
      };

      if (setDisabledTrue) {
        if (selectedHost) {
          /* 选择单主机，其他集群置灰 */
          setSingleHostDisabled();
        } else {
          /* 选择集群，单主机和其他集群置灰 */
          /* 不同集群，置灰 */
          if (clusterNode.clusterId !== selectedCluster.clusterId) {
            setOtherDisabled();
          } else {
            /* 相同集群，可选 */
            setEnable();
          }
        }
      } else {
        /* 全部可选 */
        setAllEnable();
      }
    }
  };

  if (resType === 'mount'||resType==="unMount") {
    console.log('🚀 ~ file: app.helpers.ts ~ line 353 ~ getSelectedFromTree ~ resType', resType);
  } else {
    setDisabled(result.length > 0);
  }
  // console.log(result)
  return result;
}

export function getSelectedFromList(hostArray: VMFS_CLUSTER_NODE[]) {
  let result = [];
  for (const clusterNode of hostArray) {
    if (String(clusterNode.selected) === String(ClrSelectedState.SELECTED)) {
      result.push(clusterNode);
      continue;
    }
  }
  // console.log(result)
  return result;
}

export function getVmfsCreateTreeFilterBySelect(tree) {
  return tree;
}

export function mockServerData(data, delay = 50) {
  return new Promise(resolve => {
    setTimeout(() => {
      resolve(_.cloneDeep(data) as any[]);
    }, delay);
  });
}
