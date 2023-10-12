////////LIBRARY/////////
import React, { useState, useEffect } from "react";
import * as I from "react-icons/bi";
import { toast, ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

////////ASSET/////////
import * as cst from "../../../component/Component";
import API_TOKEN from "../../../Fonction/Api_token";
import { TopBarAdmin, SideBarAdmin } from "../../../component/BasicPageAdmin";
import "./../../mainPages.css";
import { useNavigate } from "react-router-dom";
import * as DropdownMenu from "@radix-ui/react-dropdown-menu";
import * as AlertDialog from "@radix-ui/react-alert-dialog";
import { DotsHorizontalIcon } from '@radix-ui/react-icons'

export function UserListe() {
  const [recherche, setRecherche] = useState("");
  let [selectedId, setSelectedId] = useState(null);
  let [selectedPopupId, setSelectedPopupId] = useState(null);
  const navigate = useNavigate();
  let [userList, setUserList] = useState();
  const [isOpen, setIsOpen] = useState(false);
  const [showActionOptions, setShowActionOptions] = useState(false);
  const [openModal, setOpenModal] = useState(false);
  const [selectedUser, setSelectedUser] = useState([])

  
  const redirectToUserCreate = () => {
    window.location.href = "UserCreate";
  };

  //Error message if can't be deleted
  useEffect(() => {
    // Deleting a user
    const deleteUser = async () => {
      try {
        await API_TOKEN().delete(`/users/${selectedId}`);
        window.location.href = "/Pages/Admin/User/UserListe"; // Redirect immediately
        // toast.success("Utilisateur supprimé avec succès!", {
        //   autoClose: 4000,
        // });
      } catch (error) {
        console.error(error);
        toast.error("Erreur lors de la suppression de l'utilisateur");
      }
    };
    if (selectedId) {
      deleteUser();
    }
}, [selectedId]);

  function SupprimerUser(id) {
    setSelectedId(id);
    setSelectedPopupId(null);
  }

  //Call to the backend API to display all users or some users according to the input
  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await API_TOKEN().get(
          `/users/userList?search=` + recherche
        );

        setUserList(response.data);
        console.log("userList", { userList });
      } catch (error) {
        console.error(error);
      }
    };
    fetchData();
  }, [recherche]);

  //Table rows component
  const userTableRow = (user, index) => {

    //Navigate to UserModif
    const redirectToUserModif = () => {
      navigate(`/Pages/Admin/User/UserModif/${user.id}`);
    };

    //Set the selected user's name
    const handleTriggerClick = (user) => {
      setSelectedUser(user); 
      setOpenModal(true);
    };

    return (
      
      <tr key={`users-${index}`}>
        {/* lastName */}
        <cst.TdTableauCenter
          className="firstCellTable"
          onClick={redirectToUserModif}
        >
          {user.lastName}
        </cst.TdTableauCenter>

        {/* firstName */}
        <cst.TdTableauCenter
          className="firstCellTable"
          onClick={redirectToUserModif}
        >
          {user.firstName}
        </cst.TdTableauCenter>

        {/* Email */}
        <cst.TdTableauCenter
          className="firstCellTable"
          onClick={redirectToUserModif}
        >
          {user.email}
        </cst.TdTableauCenter>

        {/* JobName */}
        <cst.TdTableauCenter
          className="firstCellTable"
          onClick={redirectToUserModif}
        >
          {user.jobName}
        </cst.TdTableauCenter>

        {/* RoleName */}
        <cst.TdTableauCenter
          className="firstCellTable"
          onClick={redirectToUserModif}
        >
          {user.role}
        </cst.TdTableauCenter>

        {/* Status */}
        <cst.TdTableauCenter
          className="firstCellTable"
          onClick={redirectToUserModif}
        >
          {!user.deleted ? "Actif" : "Désactivé"}
        </cst.TdTableauCenter>

        {/* Action */}
        <cst.TdTableauCenter>

        {!user.deleted ? (

          <DropdownMenu.Root>
            <DropdownMenu.Trigger className="actionButton" onClick={ () => handleTriggerClick(user.firstName)}>
              <cst.ActionButton>
                <DotsHorizontalIcon />
              </cst.ActionButton>
            </DropdownMenu.Trigger>

            <DropdownMenu.Portal className="dropdownMenu">
              <cst.DropdownMenuContentCustom>
                <cst.DropdownMenuLabelCustom onClick={redirectToUserModif}>
                  Modifier
                </cst.DropdownMenuLabelCustom>

                <cst.DropdownMenuLabelCustom onClick={ () => handleTriggerClick(user)}>
                  Désactiver
                </cst.DropdownMenuLabelCustom>

              </cst.DropdownMenuContentCustom>
            </DropdownMenu.Portal>

          </DropdownMenu.Root>
        ) : (<div></div>) }

        </cst.TdTableauCenter> 
      </tr>
    );
  };

  // Table head component
  const userTableHead = () => {
    return (
      <thead>
        <tr>
          <cst.Th2 scope="col">Nom</cst.Th2>
          <cst.Th2 scope="col">Prénom</cst.Th2>
          <cst.Th2 scope="col">Email</cst.Th2>
          <cst.Th2 scope="col">Pôle</cst.Th2>
          <cst.Th2 scope="col">Rôle</cst.Th2>
          <cst.Th2 scope="col">Statut</cst.Th2>
          <cst.Th2 scope="col">Action</cst.Th2>
        </tr>
      </thead>
    );
  };

  //Table Body component
  const userTableBody = () => {
    return (
      <tbody>
        {userList?.users?.map((user, index) => userTableRow(user, index))}
      </tbody>
    );
  };

  return (
    <cst.Body id="Haut-page">
       <AlertDialog.Root open={openModal} onOpenChange={(value)=>!value && setOpenModal(false)}>
                    
                      <AlertDialog.Overlay/>
                      <AlertDialog.Content className="alertDialogContent">
                        <AlertDialog.Title className="alertDialogTitle">
                          Êtes-vous sûr de vouloir désactiver le compte de {selectedUser.firstName} {selectedUser.lastName}?
                        </AlertDialog.Title>
                        
                        <AlertDialog.Description>
            
                        </AlertDialog.Description>
                        <div style={{ display: 'flex', gap: 25, justifyContent: 'flex-end' }}>
                          <AlertDialog.Cancel className="modalButton cancelButton">
                              Annuler
                          </AlertDialog.Cancel>

                          <AlertDialog.Action className="modalButton confirmButton" onClick={() => setSelectedId(selectedUser.id)}>   
                              Oui, désactiver le compte                         
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
                      <cst.H6 className="titreUtili">Utilisateurs</cst.H6>
                      <div className="divBpAjout">
                        <button
                          className="bpAjout"
                          onClick={redirectToUserCreate}
                        >
                          Ajouter un utilisateur
                        </button>
                      </div>
                    </cst.Cardname>
                    <cst.Cardaffichage>
                      <cst.Cardzone>
                        <label className={"labelMainFront"}>
                          Filtre de recherche
                        </label>
                        <input
                          type="text"
                          className={"countInput"}
                          style={{ textAlign: "center", marginLeft: "0" }}
                          placeholder="Rechercher"
                          onChange={(e) => setRecherche(e.target.value)}
                        />

                        <cst.Table2>
                          {userTableHead()}
                          {userTableBody()}
                        </cst.Table2>

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

export default UserListe;
