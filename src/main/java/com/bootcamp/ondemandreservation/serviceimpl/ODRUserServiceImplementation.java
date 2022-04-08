package com.bootcamp.ondemandreservation.serviceimpl;

import com.bootcamp.ondemandreservation.model.ODRUser;
import com.bootcamp.ondemandreservation.model.ODRUserNotFoundException;
import com.bootcamp.ondemandreservation.model.Patient;
import com.bootcamp.ondemandreservation.repository.ODRUserRepository;
import com.bootcamp.ondemandreservation.security.ODRInputSanitiser;
import com.bootcamp.ondemandreservation.service.ODRUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ODRUserServiceImplementation implements  ODRUserService {
    @Autowired
    private ODRUserRepository odrUserRepository;

    public ODRUserServiceImplementation() {
    }

    public ODRUserServiceImplementation(ODRUserRepository odrUserRepository) {
        this.odrUserRepository = odrUserRepository;
    }

    @Override
    public List<ODRUser> getAllODRUsers() {
        return odrUserRepository.findAll();
    }

    @Override
    public ODRUser findODRUserById(Long id) {
        return odrUserRepository.findById(id).get();
    }

    @Override
    public ODRUser findODRUsersByEmail(String email) {
        return odrUserRepository.findByEmail(email).orElse(null);
    }

    @Override
    public ODRUser getLoggedInODRUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long id=null;
        if (principal instanceof ODRUser) {
            id = ((ODRUser)principal).getId();
        } else {
            throw new ODRUserNotFoundException();
        }
        ODRUser odrUser=findODRUserById(id);
        if(odrUser==null){
            throw new ODRUserNotFoundException();
        }
        return odrUser;
    }

    @Override
    public Map<String, String> validate(ODRUser user,boolean matchPassword,boolean forUpdate) {
        Map<String,String> rv=new HashMap<>();
        if(user.getEmail()==null||user.getEmail().isBlank()){
            rv.put("email","required");
        }else {
            if (!ODRInputSanitiser.likelyIsEmail(user.getEmail())) {
                rv.put("email", "incorrect email");
            }
            ODRUser otherUser = null;
            try {
                otherUser = findODRUsersByEmail(user.getEmail());
            } catch (Exception x) {
                x.printStackTrace();
                rv.put("email", "unsuitable email");
            }
            if (!forUpdate&&otherUser != null) {
                rv.put("email", "already registered");
            }
            if(forUpdate){
                if(findODRUsersByEmail(user.getEmail()).getId()!=user.getId()){
                    rv.put("email","wrong email for this user.");
                }
            }

        }
        if(user.getFirstName()==null||user.getFirstName().isBlank()){
            rv.put("firstName","required");
        }else if(!ODRInputSanitiser.seemsToBeSafe(user.getFirstName())){
            rv.put("firstName","invalid");
        }
        if(user.getLastName()==null||user.getLastName().isBlank()){
            rv.put("lastName","required");
        }else if(!ODRInputSanitiser.seemsToBeSafe(user.getLastName())){
            rv.put("lastName","invalid");
        }
        if(user.getPassword()==null||user.getPassword().isBlank()){
            rv.put("password","required");
        }else {
            if ((user.getPassword().length() < 6)) {
                rv.put("password", "too short");
            }
            //new password must be either blank or obey the same rules as password, except required.
            if(forUpdate&&user.getNewPassword()!=null&&!user.getNewPassword().isBlank()){
                if ((user.getNewPassword().length() < 6)) {
                    rv.put("newPassword", "too short");
                }
            }
            //no seemsToBeSafe check for password -- we store it as a hash anyway,
            //and if user wants <>`' or other unusual characters in the password, it actually makes it
            //more safe.
            if (matchPassword &&!forUpdate&& !user.getPassword().equals(user.getConfirmPassword())) {
                rv.put("confirmPassword", "does not match");
            }
            if (matchPassword &&forUpdate&& !user.getNewPassword().equals(user.getConfirmPassword())) {
                rv.put("confirmPassword", "does not match");
            }

        }

        return rv;
    }

    /**
     * Locates the user based on the username. In the actual implementation, the search
     * may possibly be case sensitive, or case insensitive depending on how the
     * implementation instance is configured. In this case, the <code>UserDetails</code>
     * object that comes back may have a username that is of a different case than what
     * was actually requested..
     *
     * @param username the username identifying the user whose data is required.
     * @return a fully populated user record (never <code>null</code>)
     * @throws UsernameNotFoundException if the user could not be found or the user has no
     *                                   GrantedAuthority
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return odrUserRepository.findByEmail(username).orElseThrow(() ->
                new UsernameNotFoundException(
                        String.format("User %s not found", username)));
    }
}
