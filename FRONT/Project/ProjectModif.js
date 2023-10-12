////////LIBRARY/////////
import React, {useState, useEffect} from "react";
import {useParams, useNavigate} from "react-router-dom";

////////ASSET/////////
import * as cst from '../../../component/Component'
import API_TOKEN from '../../../Fonction/Api_token'
import {TopBarAdmin, SideBarAdmin} from '../../../component/BasicPageAdmin'
import {toast, ToastContainer} from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

export function ProjectModif() {
    const [name, setName] = useState("");
    const [idClient, setIdClient] = useState();
    const [listClients, setListeClients] = useState();
    let {id} = useParams();
    
    const navigate = useNavigate();

    useEffect(() => {
        API_TOKEN().get(`/projects/id/${id}`).then(response => {
            setName(response.data.name)
            setIdClient(response.data.id_client)
            const idClient = response.data.id_client
            API_TOKEN().get('/clients').then(response => {
                const listClients = response.data;
                const sortedClients = listClients.sort((a, b) => {
                    if (a.id === idClient) return -1;
                    if (b.id === idClient) return 1;
                    return 0;
                })
                setListeClients(sortedClients.map((client) => {
                    return (
                        <option key={client.id} value={client.id} defaultValue={client.id === idClient}>
                            {client.name}
                        </option>
                    )
                }))
            })
        })
            .catch(response => {
                if (!response) {
                    navigate('/Pages/Admin/User/UserListe');
                }
            })
    }, [id])

    const ModifProject = async () => {
        if (name) {
            try {
                await API_TOKEN().put(`/projects/${id}`, {
                    id_client: idClient,
                    name
                });
                toast.success("Projet modifié avec succès !", {
                    autoClose: 2000,
                });
                setTimeout(() => {
                    navigate("/Pages/Admin/Project/ProjectListe");
                    window.location.reload();
                }, 3000);
            } catch (error) {
                console.error(error);
                toast.error("Une erreur s'est produite lors de la modification du projet");
            }
        } else {
            toast.error("Le nom du projet est vide");
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
                                <cst.H1>Liste des projets</cst.H1>
                            </cst.Titrebis>
                            <cst.Affichebis>
                                <cst.Interieuraffiche>
                                    <cst.Card>
                                        <cst.Cardname>
                                            <cst.H6>Modification d'un projet</cst.H6>
                                        </cst.Cardname>
                                        <cst.Cardaffichage>
                                            <cst.Cardzone>

                                                <cst.Formulaire>

                                                    <div>
                                                        <div>
                                                            <label className={"labelMainFront"}>Nom du projet</label>
                                                            <input required
                                                                value={name}
                                                                placeholder="Entrer un nom"
                                                                className={"inputMainFront"}
                                                                onChange={(e) => setName(e.target.value)}/>
                                                        </div>
                                                        
                                                        <div>
                                                            <label className={"labelMainFront"}>Client</label>
                                                            <select required
                                                                    className={"selectMainFront"}
                                                                    onChange={(e) => setIdClient(e.target.value)}>
                                                                <option>Choisir un client</option>
                                                                {listClients}
                                                            </select>
                                                        </div>

                                                        <cst.SubmitForm>
                                                            <label className={"labelMainFront"}></label>
                                                            <button className={"bpAjout"}
                                                                    onClick={ModifProject}>
                                                                Modifier le client
                                                            </button>
                                                        </cst.SubmitForm>
                                                    </div>

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

export default ProjectModif;