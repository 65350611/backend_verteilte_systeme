package com.alpha.backend.srcCode;
/*
 *********************************************************************************
 * Copyright 2020 JONATHAN SMITH
 *
 * You may not use this file except in compliance
 * with the License. You may obtain a Copy of the License by asking the
 * Copyright Owner JONATHAN SMITH
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *********************************************************************************
 */
import com.alpha.backend.srcCode.DTOs.UserToken;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Base64;


@Component
@Qualifier("UserTokenCreator")
public class UserTokenCreator {

    //Verschlusselt den Usernamen zu Base64 und gibt einen UserToken zurück.
    public UserToken createUserToken(String username) {
        return new UserToken(Base64.getEncoder().encodeToString(username.getBytes()));
    }

}
