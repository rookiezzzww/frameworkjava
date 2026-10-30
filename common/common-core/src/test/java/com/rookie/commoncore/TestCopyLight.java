package com.rookie.commoncore;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;

@Data
@NoArgsConstructor
@AllArgsConstructor
class Card {
    private String num;
}
@NoArgsConstructor
@AllArgsConstructor
@Data
class Person {
    private String name;
    private Card card;
}

@Data
@ToString
class SourcePoJoInner{
    private String username;
    private Long id;
    public InnerClass innerClass;
    @Data
    @ToString
    @AllArgsConstructor
    public static class InnerClass{
        public String innerName;
    }
}
@Data
@ToString
class TargetPoJoInner{
    private String username;
    private Long id;
    public InnerClass innerClass;
    @Data
    @ToString
    public static class InnerClass{
        public String innerName;
    }
}

public class TestCopyLight {
    @Test
    void copylight() {
//        Person sourcePerson = new Person("bite", new Card("123456"));
//        Person targetPerson = new Person();
//        BeanUtils.copyProperties(sourcePerson, targetPerson);
//        System.out.println(targetPerson);
//        sourcePerson.getCard().setNum("654321");
//        System.out.println(targetPerson);

        //修改后代码：
        Person sourcePerson = new Person("bite", new Card("123456"));
        Person targetPerson = new Person();
        BeanUtils.copyProperties(sourcePerson, targetPerson);
        Card targetCard = new Card();
        BeanUtils.copyProperties(sourcePerson.getCard(), targetCard);
        targetPerson.setCard(targetCard);
        System.out.println(targetPerson);
        sourcePerson.getCard().setNum("654321");
        System.out.println(targetPerson);
    }
    @Test
    void copyInnerClass(){
        SourcePoJoInner sourcePoJo = new SourcePoJoInner();
        sourcePoJo.setUsername("bite");
        SourcePoJoInner.InnerClass innerClass = new SourcePoJoInner.InnerClass("sourceInner");
        sourcePoJo.innerClass=innerClass;
        System.out.println(sourcePoJo);
        TargetPoJoInner targetPoJo = new TargetPoJoInner();
        BeanUtils.copyProperties(sourcePoJo,targetPoJo);
        System.out.println(targetPoJo);
//修改后代码：
//        SourcePoJoInner sourcePoJo = new SourcePoJoInner();
//        sourcePoJo.setUsername("bite");
//        SourcePoJoInner.InnerClass innerClass = new SourcePoJoInner.InnerClass("sourceInner");
//        sourcePoJo.innerClass=innerClass;
//        System.out.println(sourcePoJo);
//        //分别创建targetPoJo和targetInnerClass
//        TargetPoJoInner targetPoJo = new TargetPoJoInner();
//        TargetPoJoInner.InnerClass targetInnerClass = new TargetPoJoInner.InnerClass();
//        //再分别拷贝sourcePoJo和innerClass
//        BeanUtils.copyProperties(sourcePoJo,targetPoJo);
//        BeanUtils.copyProperties(innerClass,targetInnerClass);
//        targetPoJo.innerClass = targetInnerClass;
//        System.out.println(targetPoJo);
    }
}
