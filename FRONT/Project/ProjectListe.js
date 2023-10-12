////////LIBRARY/////////
import React, { useState, useEffect } from "react";
import * as I from "react-icons/bi";
import "react-toastify/dist/ReactToastify.css";

////////ASSET/////////
import * as cst from "../../../component/Component";
import API_TOKEN from "../../../Fonction/Api_token";
import { TopBarAdmin, SideBarAdmin } from "../../../component/BasicPageAdmin";
import convertArrayToObject from "../../../Fonction/convertArrayToObject";
import { toast, ToastContainer } from "react-toastify";
import { useNavigate } from "react-router-dom";
import * as DropdownMenu from "@radix-ui/react-dropdown-menu";
import * as AlertDialog from "@radix-ui/react-alert-dialog";
import { DotsHorizontalIcon } from "@radix-ui/react-icons";

import axios from "axios";

export function ProjectListe() {
  const [listProject, setListeProject] = useState();
  let [selectedId, setSelectedId] = useState(null);
  const [recherche, setRecherche] = useState("");
  let [selectedPopupId, setSelectedPopupId] = useState(null);
  const navigate = useNavigate();
  //const [projects, setProjects] = useState([]);
  const [projectList, setProjectList] = useState();
  const [openModal, setOpenModal] = useState(false);
  const [showActionButton, setShowActionButton] = useState(true);
  const [selectedProject, setSelectedProject] = useState([]);


  useEffect(() => {
    const deleteProject = async () => {
      try {
        await API_TOKEN().delete(`/projects/${selectedId}`);
        // toast.success("Projet supprimé avec succès !", {
        //     autoClose: 2000,
        // });
        setTimeout(() => {
          window.location.href = "/Pages/Admin/Project/ProjectListe"; // Redirection après 2 secondes
        }, 2000);
      } catch (error) {
        console.error(error);
        toast.error(
          "Une erreur s'est produite lors de la suppression du projet"
        );
      }
    };
    if (selectedId) {
      deleteProject();
    }
  }, [selectedId]);


  //Call backend API to display all projects and clients
  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await API_TOKEN().get(
          `/projects/projectList?search=` + recherche
        );

        setProjectList(response.data);
        console.log("projectList", { projectList });
      } catch (error) {
        console.error(error);
      }
    };
    fetchData();
  }, [recherche]);

  const handleTriggerClick = (project) => {
    setSelectedProject(project);
    setOpenModal(true);
  };

  const ProjectTableRow = (project, index) => {

    //Navigate to ClientModif
    const redirectToClientModif = () => {
      navigate(`/Pages/Admin/Client/ClientModif/${project.clientId}`); // Redirect using useNavigate
    };


    return (
      <tr key={`projects-${index}`}>

        {/* clientName */}
        <cst.TdTableauCenter
          className="firstCellTable"
          onClick={redirectToClientModif}
        >
          {project.clientName}
        </cst.TdTableauCenter>

        {/* projectName */}
        <cst.TdTableauCenter
          className="firstCellTable"
          onClick={redirectToClientModif}
        >
          {project.projectName}
        </cst.TdTableauCenter>

        {/* Nombre de features */}
        <cst.TdTableauCenter
          className="firstCellTable"
          onClick={redirectToClientModif}
        >
        </cst.TdTableauCenter>

        {/* Status */}
        <cst.TdTableauCenter
          className="firstCellTable"
          onClick={redirectToClientModif}
        >
          {project.actions.canBeDisabled ? "Actif" : "Désactivé"}
        </cst.TdTableauCenter>

        {/* Action */}
        <cst.TdTableauCenter>
          {!project.deleted ? (
            <DropdownMenu.Root>
              <DropdownMenu.Trigger
                className="actionButton "
                onClick={() => handleTriggerClick(project)}
              >
                <cst.ActionButton>
                  <DotsHorizontalIcon />
                </cst.ActionButton>
              </DropdownMenu.Trigger>

              <DropdownMenu.Portal>
                <cst.DropdownMenuContentCustom>
                  {!project.projectName && (
                  <cst.DropdownMenuLabelCustom onClick={redirectToClientModif}>
                    Associer à un projet 
                  </cst.DropdownMenuLabelCustom>
                   )}

                  {project.actions.canLinkClient && (
                  <cst.DropdownMenuLabelCustom onClick={() => navigate(`/Pages/Admin/Project/ProjectModif/${project.id}`)}>
                    Associer à un client 
                  </cst.DropdownMenuLabelCustom>
                   )}

                  <cst.DropdownMenuLabelCustom onClick={redirectToClientModif}>
                    Modifier
                  </cst.DropdownMenuLabelCustom>

                  <cst.DropdownMenuLabelCustom
                    onClick={() => handleTriggerClick(project)}
                  >
                    Désactiver
                  </cst.DropdownMenuLabelCustom>
                </cst.DropdownMenuContentCustom>
              </DropdownMenu.Portal>
            </DropdownMenu.Root>
          ) : (<div></div>)}
        </cst.TdTableauCenter>
      </tr>
    );
  };

  const ProjectTable = () => {
    return (
      <cst.Table2>
        <cst.tableHeader>
          <tr>
            <cst.Th2 scope="col">Client</cst.Th2>
            <cst.Th2 scope="col">Projet</cst.Th2>
            <cst.Th2 scope="col">Nombre de features</cst.Th2>
            <cst.Th2 scope="col">Statut</cst.Th2>
            <cst.Th2 scope="col">Actions</cst.Th2>
          </tr>
        </cst.tableHeader>

        <tbody>
          {projectList?.projects?.map((project, index) =>
            ProjectTableRow(project, index)
          )}
        </tbody>
      </cst.Table2>
    );
  };

  useEffect(() => {}, [recherche]);

  //Navigate to ClientCreate
  const redirectToClientCreate = () => {
    navigate(`/Pages/Admin/Client/ClientCreate`);
  };

  //RENDER
  return (
    <cst.Body id="Haut-page">

      {/* Modal */}
      <AlertDialog.Root
        open={openModal}
        onOpenChange={(value) => !value && setOpenModal(false)}
      >
        <AlertDialog.Overlay />
        <AlertDialog.Content className="alertDialogContent">
          <AlertDialog.Title className="alertDialogTitle">
            Êtes-vous sûr de vouloir désactiver le projet  {" "}
            {selectedProject.projectName}?
          </AlertDialog.Title>

          <AlertDialog.Description></AlertDialog.Description>
          <div style={{ display: "flex", gap: 25, justifyContent: "flex-end" }}>
            <AlertDialog.Cancel className="modalButton cancelButton">
              Annuler
            </AlertDialog.Cancel>

            <AlertDialog.Action
              className="modalButton confirmButton"
              onClick={() => setSelectedId(selectedProject.id)}
            >
              Oui, désactiver le projet
            </AlertDialog.Action>
          </div>
        </AlertDialog.Content>
      </AlertDialog.Root>


      {/* <ToastContainer
        className="custom-toast-container"
        position="top-center"
      /> */}


      <cst.Content>
        <SideBarAdmin />
        <cst.Contenu>
          <cst.ContenuBis>
            <TopBarAdmin />
            <cst.Affiche>
              <cst.Affichebis>
                <cst.Interieuraffiche>
                  <cst.Card>
                    <cst.Cardname>
                      <cst.H6 className="titreUtili">Projets</cst.H6>
                      <div className="divBpAjout">
                        <button
                          className="bpAjout"
                          onClick={redirectToClientCreate}
                        >
                          Créer un client
                        </button>             
                      </div>
                    </cst.Cardname>
                    <cst.Cardaffichage>
                      <cst.Cardzone>
                        <label className={"labelMainFront"}>
                          Rechercher par nom
                        </label>

                        <input
                          type="text"
                          className={"countInput"}
                          style={{ textAlign: "center", marginLeft: "0" }}
                          placeholder="Recherche par nom"
                          onChange={(e) => setRecherche(e.target.value)}
                        />

                        <ProjectTable />
                        
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

export default ProjectListe;
