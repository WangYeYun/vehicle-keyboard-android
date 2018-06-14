package com.parkingwang.vehiclekeyboard.core;

import java.util.ArrayList;
import java.util.List;

/**
 * 布局和键位混合器
 *
 * @author 陈哈哈 (yoojiachen@gmail.com)
 */
public class Mixer {

    public interface KeyTransformer {
        KeyEntry transformKey(Env env, KeyEntry key);
    }

    /**
     * 特定键位类型过滤处理
     */
    public static abstract class AbstractTypedKeyTransformer implements KeyTransformer {

        private final KeyType mKeyType;

        public AbstractTypedKeyTransformer(KeyType keyType) {
            mKeyType = keyType;
        }

        @Override
        final public KeyEntry transformKey(Env env, KeyEntry key) {
            if (mKeyType.equals(key.keyType)) {
                return transform(env, key);
            } else {
                return key;
            }
        }

        protected abstract KeyEntry transform(Env env, KeyEntry key);
    }

    private final List<KeyTransformer> mKeyTransformers = new ArrayList<>();

    /**
     * 使用键位变换器来处理布局
     *
     * @param env    Env
     * @param layout 布局
     * @return 键位列表
     */
    public List<List<KeyEntry>> mix(Env env, List<List<KeyEntry>> layout) {
        final List<List<KeyEntry>> output = new ArrayList<>();
        for (List<KeyEntry> layoutRow : layout) {
            final List<KeyEntry> row = new ArrayList<>(layoutRow.size());
            for (KeyEntry item : layoutRow) {
                KeyEntry key = item;
                for (KeyTransformer keyTransformer : mKeyTransformers) {
                    final KeyEntry ret = keyTransformer.transformKey(env, key);
                    if (null != ret) {
                        key = ret;
                    }
                }
                row.add(key);
            }
            output.add(row);
        }
        return output;
    }

    public void addMapper(KeyTransformer keyTransformer) {
        mKeyTransformers.add(keyTransformer);
    }
}
