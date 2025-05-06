package com.saa.security;

import org.mindrot.jbcrypt.BCrypt;

public class Criptografia {

    public static String gerarHash(String senha) {
        return BCrypt.hashpw(senha, BCrypt.gensalt());
    }
    public static boolean verificarSenha(String senhaInformada, String senhaHash) {
        return BCrypt.checkpw(senhaInformada, senhaHash);
    }


}
