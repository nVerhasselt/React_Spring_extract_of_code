////////LIBRARY/////////
import React, {useState, useEffect} from "react";
import {useNavigate} from "react-router-dom";

////////ASSET/////////
import * as cst from '../../../component/Component'
import API_TOKEN from '../../../Fonction/Api_token'
import {TopBarAdmin, SideBarAdmin} from '../../../component/BasicPageAdmin'
import {toast, ToastContainer} from "react-toastify";

export function ProjectCreate() {
    const [name, setName] = useState("");
    const [listClients, setListeClients] = useState()
    const [client, setClient] = useState()
    const navigate = useNavigate();

    useEffect(() => {//ListClient
        API_TOKEN().get('/clients').then(response => {
            const listClients = response.data;
            setListeClients(listClients.map((listClients) => <option key={listClients.id}
                                                                     value={listClients.id}>{listClients.name}</option>))
        })
    }, [])

    const CreateProject = async () => {
        if (name && client) {
            if (name.trim() !== name) {
                toast.error("Erreur : le nom de projet ne doit pas contenir un espace");
                return;
            }
            try {
                await API_TOKEN().post("/clients/create", {
                    id_client: client,
                    name,
                });
                toast.success("Projet créé avec succès !", {
                    autoClose: 2000,
                });
                setTimeout(() => {
                    navigate("/Pages/Admin/Project/ProjectListe");
                    window.location.reload();
                }, 3000);
            } catch (error) {
                toast.error("Une erreur s'est produite lors de la création du projet");
                console.error(error);
            }
        } else {
            toast.error("Le nom et/ou le client sont vides");
        }
    };

    return (
        <cst.Body id="Haut-page">
            <ToastContainer position="top-center"/>
            <cst.Content>
                <SideBarAdmin/>
                <cst.Contenu>
                    <cst.ContenuBis>
                        <TopBarAdmin/>
                        <cst.Affiche>
                            <cst.Titrebis>
                                <cst.H1>Projet</cst.H1>
                            </cst.Titrebis>
                            <cst.Affichebis>
                                <cst.Interieuraffiche>
                                    <cst.Card>
                                        <cst.Cardname>
                                            <cst.H6>Ajout d'un projet</cst.H6>
                                        </cst.Cardname>
                                        <cst.Cardaffichage>
                                            <cst.Cardzone>
                                                <cst.Formulaire>
                                                    <div>
                                                        <label className={"labelMainFront"}>Nom du projet</label>
                                                        <input required
                                                               value={name}
                                                               placeholder="Entrer un nom"
                                                               className={"inputMainFront"}
                                                               onChange={(e) => setName(e.target.value)}/>
                                                    </div>

                                                    {/* <cst.InfoGrp>
                                                        <label className={"labelMainFront"}>Client</label>
                                                        <select required
                                                                className={"selectMainFront"}
                                                                onChange={(e) => setClient(e.target.value)}>
                                                            <option>Choisir un client</option>
                                                            {listClients}
                                                        </select>
                                                    </cst.InfoGrp> */}

                                                    <cst.SubmitForm>
                                                        <label className={"labelMainFront"}></label>
                                                        <button className={"bpAjout"}
                                                                onClick={CreateProject}>
                                                            Ajouter le projet
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

export default ProjectCreate;