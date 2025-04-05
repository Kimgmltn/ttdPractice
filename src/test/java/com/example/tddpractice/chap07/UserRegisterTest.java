package com.example.tddpractice.chap07;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserRegisterTest {
    private UserRegister userRegister;
    private StubWeakPasswordChecker stubWeakPasswordChecker = new StubWeakPasswordChecker();
    private MemoryUserRepository fakeRepository = new MemoryUserRepository();
    private SpyEmailNotifier spyEmailnotifier = new SpyEmailNotifier();

    @BeforeEach
    void setUp(){
        this.userRegister = new UserRegister(stubWeakPasswordChecker, fakeRepository, spyEmailnotifier);
    }

    @DisplayName("약한 암호면 가입 실패")
    @Test
    void weakPassword(){
        stubWeakPasswordChecker.setWeak(true);

        assertThrows(WeakPasswordException.class, () -> {
            userRegister.register("id", "pw", "email");
        });
    }

    @DisplayName("이미 같은 ID가 존재하면 가입 실패")
    @Test
    void dupldExists() {
        fakeRepository.save(new User("id", "pw1", "email@email.com"));
        assertThrows(DupldException.class, () -> {
            userRegister.register("id", "pw2", "email");
        });
    }

    @DisplayName("같은 ID가 없으면 가입 성공함")
    @Test
    void noDupld_RegisterSuccess(){
        userRegister.register("id","pw","email");

        User savedUser = fakeRepository.findById("id");
        assertEquals("id",savedUser.getId());
        assertEquals("email",savedUser.getEmail());
    }

    @DisplayName("가입하면 메일을 전송함")
    @Test
    void whenRegisterThenSendMail(){
        userRegister.register("id","pw","email@email.com");
        assertTrue(spyEmailnotifier.isCalled());
        assertEquals("email@email.com", spyEmailnotifier.getEmail());
    }
}

