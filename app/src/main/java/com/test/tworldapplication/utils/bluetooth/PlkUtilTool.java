package com.test.tworldapplication.utils.bluetooth;

import java.io.CharArrayWriter;
import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dasiy on 16/11/14.
 */

public class PlkUtilTool {
    public static final String[] HexCode = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};

    public PlkUtilTool() {
    }

    public static byte[] getBCD(String phone) {
        if(phone.length() % 2 > 0) {
            phone = phone + "F";
        }

        if(phone == null) {
            return null;
        } else {
            byte[] result = new byte[phone.length() / 2];

            for(int i = 0; i < result.length; ++i) {
                int x = Integer.parseInt(phone.substring(i * 2, i * 2 + 2), 16);
                result[i] = x <= 127?(byte)x:(byte)(x - 256);
            }

            return result;
        }
    }

    public static byte[] getUnicode(String str) {
        char[] chars = str.toCharArray();
        byte[] result = new byte[chars.length * 2];

        for(int i = 0; i < chars.length; ++i) {
            result[2 * i] = (byte)(chars[i] / 256);
            result[2 * i + 1] = (byte)(chars[i] % 256);
        }

        return result;
    }

    public static String getCASCIIString(byte[] data, int offset, int size) {
        CharArrayWriter out = new CharArrayWriter();

        for(int i = offset; i < size + offset && data[i] != 0; ++i) {
            out.write((char)data[i]);
        }

        return out.toString();
    }

    public static String byteArrayToHexString(byte[] b, int offset, int size) {
        if(b == null) {
            return null;
        } else {
            String result = "";

            for(int i = offset; i < offset + size; ++i) {
                result = result + byteToHexString(b[i]);
            }

            return result;
        }
    }

    public static String byteArrayToHexString(byte[] b) {
        if(b == null) {
            return null;
        } else {
            String result = "";

            for(int i = 0; i < b.length; ++i) {
                result = result + byteToHexString(b[i]);
            }

            return result;
        }
    }

    public static String byteArrayWithLenToHexString(byte[] b, int off, int len) {
        if(b == null) {
            return null;
        } else {
            String result = "";

            for(int i = 0; i < len; ++i) {
                result = result + byteToHexString(b[off + i]);
            }

            return result;
        }
    }

    public static byte[] hexStringToByteArray(String text) {
        if(text == null) {
            return null;
        } else {
            byte[] result = new byte[text.length() / 2];

            for(int i = 0; i < result.length; ++i) {
                int x = Integer.parseInt(text.substring(i * 2, i * 2 + 2), 16);
                result[i] = x <= 127?(byte)x:(byte)(x - 256);
            }

            return result;
        }
    }

    public static byte[] hexStringToAsciiByteArray(String text, int byteLen) {
        if(text == null) {
            return null;
        } else {
            byte[] result = new byte[byteLen > text.length()?byteLen:text.length()];

            for(int i = 0; i < text.length(); ++i) {
                int x = Integer.parseInt(text.substring(i, i + 1), 16);
                result[i] = (byte)(48 + x);
            }

            return result;
        }
    }

    public static String hexStringToString(String hexString, String charSet) {
        if(hexString == null) {
            return null;
        } else {
            String result = "";

            try {
                result = new String(hexStringToByteArray(hexString), charSet);
            } catch (Exception var4) {
                ;
            }

            return result;
        }
    }

    public static String hexStringToAsciiString(String hexString) {
        return hexStringToString(hexString, "ASCII");
    }

    public static String byteToHexString(byte b) {
        int n = b;
        if(b < 0) {
            n = b + 256;
        }

        int d1 = n / 16;
        int d2 = n % 16;
        return HexCode[d1] + HexCode[d2];
    }

    public static String longMoney2String(long money) {
        float m = (float)money / 100.0F;
        return String.valueOf(m) + "元";
    }

    public static String ascNumStr2HexStr(String ascStr) {
        StringBuilder sb = new StringBuilder();
        if(ascStr.length() % 2 == 0) {
            for(int i = 0; i < ascStr.length(); i += 2) {
                sb.append(ascStr.substring(i + 1, i + 2));
            }
        }

        return sb.toString();
    }

    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    public static double doubleRound(double value, int scale, int roundingMode) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(scale, roundingMode);
        double d = bd.doubleValue();
        bd = null;
        return d;
    }

    public static String getSWofApdu(byte[] res) {
        return byteArrayToHexString(res).substring(res.length * 2 - 4);
    }

    public static boolean arrayCompareWithByte(byte[] arr, int off, int len, byte compareTo) {
        for(int i = 0; i < len; ++i) {
            if(arr[off + i] != compareTo) {
                return false;
            }
        }

        return true;
    }

    public static void arrayFillWithByte(byte[] arr, int off, int len, byte fillWith) {
        for(int i = 0; i < len; ++i) {
            arr[off + i] = fillWith;
        }

    }

    public static boolean arrayCopy(byte[] s_arr, int s_off, byte[] t_arr, int t_off, int len) {
        if(s_off + len > s_arr.length) {
            return false;
        } else if(t_off + len > t_arr.length) {
            return false;
        } else if(len <= 0) {
            return false;
        } else {
            int i;
            if(s_arr == t_arr) {
                if(t_off > s_off) {
                    for(i = len - 1; i >= 0; --i) {
                        t_arr[t_off + i] = s_arr[s_off + i];
                    }
                } else if(t_off < s_off) {
                    for(i = 0; i < len; ++i) {
                        t_arr[t_off + i] = s_arr[s_off + i];
                    }
                }
            } else {
                for(i = len - 1; i >= 0; --i) {
                    t_arr[t_off + i] = s_arr[s_off + i];
                }
            }

            return true;
        }
    }

    public static Byte getRespSW1(byte[] resp) {
        return resp.length < 2?null:Byte.valueOf(resp[resp.length - 2]);
    }

    public static Byte getRespSW2(byte[] resp) {
        return resp.length < 2?null:Byte.valueOf(resp[resp.length - 1]);
    }

    public static String getRespSW(byte[] resp) {
        return resp.length < 2?null:byteArrayWithLenToHexString(resp, resp.length - 2, 2);
    }

    public static String getRespData(byte[] resp) {
        return resp.length <= 2?null:byteArrayWithLenToHexString(resp, 0, resp.length - 2);
    }

    public static short byte2Short(byte B) {
        return (short)(B & 255);
    }

    public static int byteArray2int(byte[] arr, int off, byte len) {
        int res = 0;

        for(byte i = 0; i < len; ++i) {
            res *= 256;
            short temp = byte2Short(arr[off + i]);
            res += temp;
        }

        return res;
    }

    public static byte getBitValueByIndex(byte[] bit_array, short off, short index) {
        short byte_index = (short)(index / 8);
        byte bit_index = (byte)(index % 8);
        if(byte_index + off >= bit_array.length) {
            return (byte)0;
        } else {
            byte v = (byte)(1 << bit_index);
            return (byte)((byte)(bit_array[(short)(off + byte_index)] & v) != 0?1:0);
        }
    }

    public static void setBitValueByIndex(byte[] bit_array, short off, short index, byte value) {
        short byte_index = (short)(index / 8);
        byte bit_index = (byte)(index % 8);
        if(byte_index + off < bit_array.length) {
            byte v;
            if(value == 0) {
                v = (byte)(1 << bit_index);
                v = (byte)(~v);
                bit_array[(short)(off + byte_index)] &= v;
            } else {
                v = (byte)(1 << bit_index);
                bit_array[(short)(off + byte_index)] |= v;
            }

        }
    }

    public static boolean isBitAllSetWithValue(byte[] bit_array, short off, short bitNum, byte value) {
        if(bitNum < 1) {
            return false;
        } else {
            short byte_index = (short)((bitNum - 1) / 8);
            byte bit_index = (byte)((bitNum - 1) % 8);

            for(short i = 0; i < byte_index - 1; ++i) {
                if(value == 0) {
                    if(bit_array[off + i] != 0) {
                        return false;
                    }
                } else if(bit_array[off + i] != -1) {
                    return false;
                }
            }

            for(byte var7 = 0; var7 <= bit_index; ++var7) {
                if(getBitValueByIndex(bit_array, (short)(off + byte_index), var7) != value) {
                    return false;
                }
            }

            return true;
        }
    }
}
