package com.bootcamp.ondemandreservation.serviceimpl;

import com.bootcamp.ondemandreservation.model.ODRUser;
import com.bootcamp.ondemandreservation.model.ODRUserNotFoundException;
import com.bootcamp.ondemandreservation.model.Patient;
import com.bootcamp.ondemandreservation.repository.ODRUserRepository;
import com.bootcamp.ondemandreservation.service.ODRUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return odrUserRepository.findByEmail(email).get();
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
