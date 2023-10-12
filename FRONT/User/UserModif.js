////////LIBRARY/////////
import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";

////////ASSET/////////
import * as cst from '../../../component/Component'
import API_TOKEN from '../../../Fonction/Api_token'
import { TopBarAdmin, SideBarAdmin } from '../../../component/BasicPageAdmin'
import { toast, ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

export function UserModif() {
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [email, setEmail] = useState("");
    const [username, setUsername] = useState("");
    const [newPassword, setNewPassword] = useState("");
    const [newPassword2, setNewPassword2] = useState("");
    const verifMail = /^[\w.-]+@[a-zA-Z_-]+?(?:\.[a-zA-Z]{2,})+$/
    let {id} = useParams();
    const navigate = useNavigate();

    
    useEffect(() => {
        // Fetch user details for editing
        API_TOKEN().get(`/users/${id}`)
            .then(response => {
                setFirstName(response.data.firstName);
                setLastName(response.data.lastName);
                setEmail(response.data.email);
                setUsername(response.data.username);
            })
            .catch(response => {
                if (response) {
                    navigate('/Pages/Admin/User/UserListe');
                }
            })
    }, [id])

    
    const ModifUser = async () => {
        // Fetch the list of users for username verification
        API_TOKEN().get('/users/verif').then(response => {
            const listUser = response.data;
            if (listUser) {
                const e = listUser.map((listUser) => listUser.username === username);
                let test = false
                for (let i = 0; i < e.length; i++) {
                    if (e[i] === true) {
                        test = true
                    }
                }
                if (!firstName || !lastName || !email || !username || !newPassword || !newPassword2) {
                    toast.error("Erreur : tous les champs obligatoires n'ont pas été remplis");
                } else if (newPassword !== newPassword2) {
                    toast.error("Erreur : les mots de passe ne correspondent pas");
                } else if (!verifMail.test(email)) {
                    toast.error("Erreur : l'adresse mail n'est pas valide");
                } else {
                    // Update user information
                    API_TOKEN()
                        .put(`/users/${id}`, {
                            firstName: firstName,
                            lastName: lastName,
                            email: email,
                            username: username,
                            password: newPassword,
                        })
                        .then(() => {
                            toast.success("Utilisateur modifié avec succès !", {
                                autoClose: 2000,
                            });
                            setTimeout(() => {
                                navigate("/Pages/Admin/User/UserListe");
                                window.location.reload();
                            }, 3000);
                        })
                        .catch((e) => {
                            return { error: true, message: JSON.stringify(e) };
                        });
                }
            }
        })
    }

    return (
        <cst.Body id="Haut-page">
            <ToastContainer position="top-center" />
            <cst.Content>
                <SideBarAdmin />
                <ToastContainer position="top-center" />
                <cst.Contenu>
                    <cst.ContenuBis>
                        <TopBarAdmin />
                        <cst.Affiche>
                            <cst.Titrebis>
                                <cst.H1>Liste des utilisateurs</cst.H1>
                            </cst.Titrebis>
                            <cst.Affichebis>
                                <cst.Interieuraffiche>
                                    <cst.Card>
                                        <cst.Cardname>
                                            <cst.H6>Modification d'un utilisateur</cst.H6>
                                        </cst.Cardname>
                                        <cst.Cardaffichage>
                                            <cst.Cardzone>

                                                <cst.Formulaire>

                                                    <cst.NameGrp>
                                                        <div>
                                                            <label className={"labelMainFront"}>Prénom</label>
                                                            <input required
                                                                value={firstName}
                                                                placeholder="Entrer un prénom"
                                                                className={"inputMainFront"}
                                                                onChange={(e) => setFirstName(e.target.value)} />
                                                        </div>
                                                        <div>
                                                            <label className={"labelMainFront"}>Nom</label>
                                                            <input required
                                                                value={lastName}
                                                                placeholder="Entrer un nom"
                                                                className={"inputMainFront"}
                                                                onChange={(e) => setLastName(e.target.value)} />
                                                        </div>
                                                    </cst.NameGrp>

                                                        <div>
                                                            <label className={"labelMainFront"}>Email</label>
                                                            <input required
                                                                value={email}
                                                                type="email"
                                                                placeholder="Entrer un email"
                                                                className={"inputMainFront"}
                                                                onChange={(e) => setEmail(e.target.value)} />
                                                        </div>
                                                        <div>
                                                            <label className={"labelMainFront"}>Pseudo</label>
                                                            <input required
                                                                value={username}
                                                                placeholder="Entrer un pseudo"
                                                                className={"inputMainFront"}
                                                                onChange={(e) => setUsername(e.target.value)} />
                                                        </div>
                                                        <div>
                                                            <label className={"labelMainFront"}>Mot de passe</label>
                                                            <input required
                                                                type="password"
                                                                placeholder="Entrer un mot de passe"
                                                                className={"inputMainFront"}
                                                                onChange={(e) => setNewPassword(e.target.value)} />
                                                        </div>
                                                        <div>
                                                            <label className={"labelMainFront"}>Confirmer le mot de passe</label>
                                                            <input required
                                                                type="password"
                                                                placeholder="Confirmer le mot de passe"
                                                                className={"inputMainFront"}
                                                                onChange={(e) => setNewPassword2(e.target.value)} />
                                                        </div>

                                                        <cst.SubmitForm>
                                                            <label className={"labelMainFront"}></label>
                                                            <button className={"bpAjout"}
                                                                onClick={ModifUser}>
                                                                Enregistrer les modifications
                                                            </button>
                                                        </cst.SubmitForm>
                                                
                                                </cst.Formulaire>

                                            </cst.Cardzone>
                                        </cst.Cardaffichage>
                                    </cst.Card>
                                </cst.Interieuraffiche>
                            </cst.Affichebis>
                        </cst.Affiche>
                    </cst.ContenuBis>
                </cst.Contenu>
            </cst.Content>
        </cst.Body>
    );
}

export default UserModif;