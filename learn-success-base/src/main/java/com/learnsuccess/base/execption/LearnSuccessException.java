package com.learnsuccess.base.execption;

/**
 * @description 学成在线项目异常类
 * @author Mr.M
 * @date 2022/9/6 11:29
 * @version 1.0
 */
public class LearnSuccessException extends RuntimeException {

   private String errMessage;

   public LearnSuccessException() {
      super();
   }

   public LearnSuccessException(String errMessage) {
      super(errMessage);
      this.errMessage = errMessage;
   }

   public String getErrMessage() {
      return errMessage;
   }

   public static void cast(CommonError commonError){
       throw new LearnSuccessException(commonError.getErrMessage());
   }
   public static void cast(String errMessage){
       throw new LearnSuccessException(errMessage);
   }

}
