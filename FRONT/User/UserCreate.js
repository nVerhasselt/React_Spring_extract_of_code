////////LIBRARY/////////
import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";

////////ASSET/////////
import * as cst from '../../../component/Component'
import API_TOKEN from '../../../Fonction/Api_token'
import { TopBarAdmin, SideBarAdmin } from '../../../component/BasicPageAdmin'
import { toast, ToastContainer } from "react-toastify";
import "./../../mainPages.css"

export function CreatUser() {
    const [prenom, setPrenom] = useState("");
    const [nom, setNom] = useState("");
    const [mail, setMail] = useState("");
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [password2, setPassword2] = useState("");
    const [listJobs, setListJob] = useState();
    const [selectedJob, setJob] = useState()
    const [listRoles, setListeRoles] = useState()
    const [selectedRole, setRole] = useState()
    const verifChiffre = /^[0-9]+$/;
    const verifMail = /^[\w.-]+@[a-zA-Z_-]+?(?:\.[a-zA-Z]{2,})+$/
    const navigate = useNavigate();

    useEffect(() => {
        // Fetch roles and jobs for dropdown menus
        API_TOKEN().get('/roles').then(roles => {
            const listRoles = roles.data;
            API_TOKEN().get('/job').then(job => {
                const listeJob = job.data;
                setListeRoles(listRoles.map((listRoles) => <option key={listRoles.id}
                    value={listRoles.id}>{listRoles.name}</option>))
                setListJob(listeJob.map((listJobs) => <option key={listJobs.id}
                    value={listJobs.id}>{listJobs.name}</option>))
            })
        })
    }, [])

    const CreateUser = async () => {
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
                if (!prenom || !nom || !mail || !username || !password || !verifChiffre.test(selectedJob) || !verifChiffre.test(selectedRole)) {
                    toast.error("Erreur : tous les champs n'ont pas été remplis");
                }

                if (username.trim() !== username) {
                    toast.error("Erreur : l'identifiant ne doit pas contenir un espace");
                    return;
                }
                if (prenom.trim() !== prenom) {
                    toast.error("Erreur : Le prénom ne doit pas contenir un espace");
                    return;
                }
                else if (password !== password2) {
                    toast.error("Erreur : les mots de passe ne correspondent pas");
                } else if (!verifMail.test(mail)) {
                    toast.error("Erreur : l'adresse mail n'est pas valide");
                } else if (test === true) {
                    toast.error("Erreur : l'identifiant est déjà utilisé");
                } else {
                    // Create a new user
                    API_TOKEN()

                        .post('/users/create', {
                            firstName: prenom,
                            lastName: nom,
                            email: mail,
                            username: username,
                            password: password,
                            id_job: selectedJob,
                            id_role: selectedRole
                        })
                        .then(() => {
                            toast.success("Utilisateur créé avec succès !", {
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
                <cst.Contenu>
                    <cst.ContenuBis>
                        <TopBarAdmin />
                        <cst.Affiche>
                            <cst.Titrebis>
                                <cst.H1>Utilisateurs</cst.H1>
                            </cst.Titrebis>
                            <cst.Affichebis>
                                <cst.Interieuraffiche>
                                    <cst.Card>
                                        <cst.Cardname>
                                            <cst.H6>Créer un utilisateur</cst.H6>
                                        </cst.Cardname>
                                        <cst.Cardaffichage>
                                            <cst.Cardzone>
                                                
                                                <cst.Formulaire>

                                                    <cst.NameGrp>
                                                        <div>
                                                            <label>Prénom</label>
                                                            <input required
                                                                placeholder="Entrer un prénom"
                                                                className={"inputMainFront"}
                                                                onChange={(e) => setPrenom(e.target.value)} />
                                                        </div>

                                                        <div>
                                                            <label>Nom</label>
                                                            <input required
                                                                placeholder="Entrer un nom"
                                                                className={"inputMainFront"}
                                                                onChange={(e) => setNom(e.target.value)} />
                                                        </div>
                                                    </cst.NameGrp>

                                                    <cst.InfoGrp>
                                                        <div>
                                                            <label className={"labelMainFront"}>Email</label>
                                                            <input required
                                                                type="email"
                                                                placeholder="Entrer un email"
                                                                className={"inputMainFront"}
                                                                onChange={(e) => setMail(e.target.value)} />
                                                        </div>

                                                        <div>
                                                            <label className={"labelMainFront"}>Pseudo</label>
                                                            <input required
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
                                                                onChange={(e) => setPassword(e.target.value)} />
                                                        </div>

                                                        <div>
                                                            <label className={"labelMainFront"}>Confirmer le mot de passe</label>
                                                            <input required
                                                                type="password"
                                                                placeholder="Confirmer le mot de passe"
                                                                className={"inputMainFront"}
                                                                onChange={(e) => setPassword2(e.target.value)} />
                                                        </div>
                                                    

                                                        <div>
                                                            <label className={"labelMainFront"}>Pôle</label>
                                                            <select required
                                                                name="job"
                                                                id="job"
                                                                className={"selectMainFront"}
                                                                onChange={(e) => setJob(e.target.value)}>
                                                                <option value={0}>Choisir un pôle</option>
                                                                {listJobs}
                                                            </select>
                                                        </div>
                                                        <div>
                                                            <label className={"labelMainFront"}>Rôle</label>
                                                            <select required="role"
                                                                id="role"
                                                                className={"selectMainFront"}
                                                                onChange={(e) => setRole(e.target.value)}>
                                                                <option value={0}>Choisir un rôle</option>
                                                                {listRoles}
                                                            </select>
                                                        </div>
                                                    </cst.InfoGrp>

                                                    <cst.SubmitForm>
                                                        <label className={"labelMainFront"}></label>
                                                        <button className={"bpAjout"}
                                                            onClick={CreateUser}>
                                                            Créer un utilisateur
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

export default CreatUser;