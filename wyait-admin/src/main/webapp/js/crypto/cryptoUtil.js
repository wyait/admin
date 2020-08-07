/**
 * 加解密工具类
 * */
/**
* AES加密 
* @param contentParam 加密内容 
* @param keyParam 加密密码，由字母或数字组成 
　　　　　　此方法使用AES-128-CBC加密模式，key需要为16位 
　　　　　　加密解密key必须相同
* @return 加密密文 
*/
var ivParam = "abcdef1234567890";
function encryptKey(contentParam, keyParam){
	if(contentParam=='' || keyParam==''){
		return;
	}
    var key = CryptoJS.enc.Utf8.parse(keyParam);
    var iv = CryptoJS.enc.Utf8.parse(ivParam);
    var content = CryptoJS.enc.Utf8.parse(contentParam);
    var encryptData = CryptoJS.AES.encrypt(content, key, {iv:iv,mode:CryptoJS.mode.CBC,padding: CryptoJS.pad.Pkcs7});
    return encryptData.toString();
}

/**
* AES解密 
* @param contentParam 加密密文 
* @param keyParam 加密密码，由字母或数字组成 
　　　　　　此方法使用AES-128-CBC加密模式，key需要为16位 
　　　　　　加密解密key必须相同
* @return 解密明文 
*/
function decryptKey(contentParam, keyParam){
    var key = CryptoJS.enc.Utf8.parse(keyParam);
    var iv = CryptoJS.enc.Utf8.parse(ivParam);
    var decryptData = CryptoJS.AES.decrypt(contentParam, key, {iv:iv,mode:CryptoJS.mode.CBC,padding: CryptoJS.pad.Pkcs7});
    return CryptoJS.enc.Utf8.stringify(decryptData).toString();
}

var keyData=".admin.wyait.com";
function encrypt(contentParam){
	if(contentParam==''){
		return;
	}
    var key = CryptoJS.enc.Utf8.parse(keyData);
    var iv = CryptoJS.enc.Utf8.parse(ivParam);
    var content = CryptoJS.enc.Utf8.parse(contentParam);
    var encryptData = CryptoJS.AES.encrypt(content, key, {iv:iv,mode:CryptoJS.mode.CBC,padding: CryptoJS.pad.Pkcs7});
    return encryptData.toString();
}
function decrypt(contentParam){
    var key = CryptoJS.enc.Utf8.parse(keyData);
    var iv = CryptoJS.enc.Utf8.parse(ivParam);
    var decryptData = CryptoJS.AES.decrypt(contentParam, key, {iv:iv,mode:CryptoJS.mode.CBC,padding: CryptoJS.pad.Pkcs7});
    return CryptoJS.enc.Utf8.stringify(decryptData).toString();
}


/**
 * 字符串转字节数组
 * @param str
 * @returns {Array}
 */
function stringToByte(str) {
	var bytes = new Array();
	var len, c;
	len = str.length;
	for(var i = 0; i < len; i++) {
		c = str.charCodeAt(i);
		if(c >= 0x010000 && c <= 0x10FFFF) {
			bytes.push(((c >> 18) & 0x07) | 0xF0);
			bytes.push(((c >> 12) & 0x3F) | 0x80);
			bytes.push(((c >> 6) & 0x3F) | 0x80);
			bytes.push((c & 0x3F) | 0x80);
		} else if(c >= 0x000800 && c <= 0x00FFFF) {
			bytes.push(((c >> 12) & 0x0F) | 0xE0);
			bytes.push(((c >> 6) & 0x3F) | 0x80);
			bytes.push((c & 0x3F) | 0x80);
		} else if(c >= 0x000080 && c <= 0x0007FF) {
			bytes.push(((c >> 6) & 0x1F) | 0xC0);
			bytes.push((c & 0x3F) | 0x80);
		} else {
			bytes.push(c & 0xFF);
		}
	}
	return bytes;


}

/**
 * 字节数组转字符串
 * @param arr
 * @returns
 */
 function byteToString(arr) {
	if(typeof arr === 'string') {
		return arr;
	}
	var str = '',
		_arr = arr;
	for(var i = 0; i < _arr.length; i++) {
		var one = _arr[i].toString(2),
			v = one.match(/^1+?(?=0)/);
		if(v && one.length == 8) {
			var bytesLength = v[0].length;
			var store = _arr[i].toString(2).slice(7 - bytesLength);
			for(var st = 1; st < bytesLength; st++) {
				store += _arr[st + i].toString(2).slice(2);
			}
			str += String.fromCharCode(parseInt(store, 2));
			i += bytesLength - 1;
		} else {
			str += String.fromCharCode(_arr[i]);
		}
	}
	return str;
}


//十六进制字符串转字节数组
 function stringToBytes(str)
 {
     var pos = 0;
     var len = str.length;
     if(len %2 != 0)
     {
        return null; 
     }
     len /= 2;
     var hexA = new Array();
     for(var i=0; i<len; i++)
     {
        var s = str.substr(pos, 2);
        var v = parseInt(s, 16);
        hexA.push(v);
        pos += 2;
     }
     return hexA;
 }
  
 //字节数组转十六进制字符串
 function bytesToString(arr)
 {
     var str = "";
     for(var i=0; i<arr.length; i++)
     {
        var tmp = arr[i].toString(16);
        if(tmp.length == 1)
        {
            tmp = "0" + tmp;
        }
        str += tmp;
     }
     return str;
  
 }


//字节数组转十六进制字符串，对负值填坑
 function bytesHexString(arrBytes) {
   var str = "";
   for (var i = 0; i < arrBytes.length; i++) {
     var tmp;
     var num=arrBytes[i];
     if (num < 0) {
     //此处填坑，当byte因为符合位导致数值为负时候，需要对数据进行处理
       tmp =(255+num+1).toString(16);
     } else {
       tmp = num.toString(16);
     }
     if (tmp.length == 1) {
       tmp = "0" + tmp;
     }
     str += tmp;
   }
   return str;
 }











