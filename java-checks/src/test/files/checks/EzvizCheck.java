/**
 *
 */
package com.ys.product.userdevice.biz.ezviz.util;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

/**
 * MyBatis的Session Utility类
 *
 * @author mingxu
 */
public final class MyBatisSessionUtils {

    public interface MyBatisBatchCallback<Result> {

        Result doInBatch(SqlSession session);

    }

    private MyBatisSessionUtils() {
    }

    /**
     * 创建一个BatchSession的实例
     *
     * @param fac
     * @return
     */
    public static SqlSession createBatchSqlSession(SqlSessionFactory fac) {
        return fac.openSession(ExecutorType.BATCH);
    }

    public static <Result> Result batchUpdate(SqlSessionFactory fac, MyBatisBatchCallback<Result> batchCallback) {
        try (SqlSession session = fac.openSession(ExecutorType.BATCH);) {
            Result doInBatch = batchCallback.doInBatch(session);
            session.commit();
            return doInBatch;
        }
    }
}