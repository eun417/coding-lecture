package hello.hellospring.domain;

import jakarta.persistence.*;

/**
 * ORM = Object Relational table Mapping
 * */
@Entity
public class Member {

    /**
     * Indentity 전략
     * : Query에 아이디를 넣는 게 아니라 DB에 값을 넣으면 DB가 아이디를 자동 생성해줌
     * */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    //고객 구분을 위해 시스템이 저장하는 아이디

    //DB에 있는 USERNAME 컬럼명과 연결
//    @Column(name = "username")
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
