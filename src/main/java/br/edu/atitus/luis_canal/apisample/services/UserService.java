package br.edu.atitus.luis_canal.apisample.services;

import br.edu.atitus.luis_canal.apisample.entities.User;
import br.edu.atitus.luis_canal.apisample.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository repository;

    private final PasswordEncoder encoder;

    public UserService(UserRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    public User save(User newUser) throws Exception {
        if (newUser == null)
            throw new Exception("Objeto Nulo!");

        if (newUser.getName() == null || newUser.getName().isBlank())
            throw new Exception("Nome informado inválido!");
        newUser.setName(newUser.getName().trim());

        if (newUser.getEmail() == null || newUser.getEmail().isBlank())
            throw new Exception("E-mail informado inválido!");
        newUser.setEmail(newUser.getEmail().trim().toLowerCase());
        
        if (!validateEmail(newUser.getEmail()))
            throw new Exception("E-mail deve estar em um dos domínios permitidos: gmail.com, bol.com.br ou yahoo.com");

        if (repository.existsByEmail(newUser.getEmail()))
            throw new Exception("Já existe usuário cadastrado com este e-mail!");

        if (newUser.getPassword() == null || newUser.getPassword().length() < 8)
            throw new Exception("Senha deve ter no mínimo 8 caracteres!");
        
        if (!validatePassword(newUser.getPassword()))
            throw new Exception("Senha deve conter pelo menos uma letra maiúscula, uma minúscula e um número!");
        
        newUser.setPassword(encoder.encode(newUser.getPassword()));


        if (newUser.getType() == null)
            throw new Exception("Tipo de usuário informado inválido!");

        // Solicita para camada Repository salvar o registro
        // E retorna o registro salvo
        return repository.save(newUser);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return (UserDetails) repository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com esse email"));
    }

    private boolean validateEmail(String email) {
        String regex = "^[a-zA-Z0-9._%-]+@(gmail\\.com|bol\\.com\\.br|yahoo\\.com)$";
        return email.matches(regex);
    }

    private boolean validatePassword(String password) {
        String regex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$";
        return password.matches(regex);
    }
}
