import React, { useState, useEffect } from 'react';
import * as cst from '../../component/Component';
import { TopBarAdmin, SideBarAdmin } from '../../component/BasicPageAdmin';
import API_TOKEN from '../../Fonction/Api_token';
import "../mainPages.css";
import styled, { keyframes } from 'styled-components';
//import {TableAdminDashboard} from '../../Fonction/ListeTableau';


import { get, isUndefined } from 'lodash';
import Accordion from '../../component/Accordion';


function Admin_Dashboard() {
    const [numberOfWeeks, setNumberOfWeeks] = useState(5);
    const [reloadComponents, setReloadComponents] = useState(false);
    const [weekList, setWeekList] = useState([]);

    useEffect(() => {
      if(!isUndefined(numberOfWeeks)) {
          const fetchData = async () => {
            // Sends a POST request to the '/tasks/weeks' endpoint passing the weekCount parameter with the value of numberOfWeeks.
              API_TOKEN().post('/tasks/weeks', {
                  "weekCount": numberOfWeeks,
            // List of weeks object from the API
              }).then(response=> {
                setWeekList(response.data)
              })
          };
          fetchData().catch(console.error);
      }
        console.log('weekList', weekList);
    }, [numberOfWeeks]);



    //reload when NumberOfWeeks changes//
    const handleNumberOfWeeksChange = (e) => {
        const isValid = /^[1-9]\d*$/.test(e.target.value);
        if (isValid) {
            setNumberOfWeeks(e.target.value);
            setReloadComponents(!reloadComponents);
        }
    };


    // User and worktime
    const renderUser = ({worktime, userName}) => (
        <cst.UserContainer key={`user-${userName}`} 
        worktime={worktime} 
        userName={userName} 
        displayRed={worktime > 35} 
        displayGreen={worktime == 35} 
        displayYellow={worktime < 35}>
            <cst.UserName worktime={worktime} userName={userName}>               
                {userName}
            </cst.UserName>
            <div>{worktime} h</div>
        </cst.UserContainer>
    )

    // Week card of user and worktime
    const renderWeek = (week, weekIndex) => week.users?.length > 0 ? (   
        <Accordion key={`week-${weekIndex}`} weekNumber={week.weekNumber} >
            {week.users.map((user, userIndex) => renderUser(user))}
        </Accordion>
    ) : null

    // List of cards
    const renderWeekList = () => (
        <cst.CardListContainer>
            {weekList.weeks.map((week, index) => renderWeek(week, index))}
        </cst.CardListContainer>
    )

    return (
        <cst.Body id="Haut-page">
            <cst.Content>

                {/*SideBarAdmin */}
                <SideBarAdmin/>

                <cst.Contenu>
                    <cst.ContenuBis>
                        <TopBarAdmin />
                        <cst.Affiche>
                            <cst.Titrebis>
                                <cst.H1>Dashboard Administrateur</cst.H1>
                            </cst.Titrebis>
                            <cst.Affichebis>
                                <cst.Interieuraffiche>
                                    <cst.Card>
                                        <cst.Cardname>
                                            <cst.H6>Affichage des {numberOfWeeks} dernières semaines</cst.H6>
                                        </cst.Cardname>
                                        <cst.Cardaffichage>
                                            <cst.Cardzone>
                                                <div className={"divTwoColumns"}>
                                                    <div>
                                                        <label className={"labelMainFront"}>Nombre de semaines à
                                                            afficher</label>
                                                        <input
                                                            type="text"
                                                            className={"countInput"}
                                                            placeholder="Nombre de jours à afficher"
                                                            defaultValue={numberOfWeeks}
                                                            onChange={handleNumberOfWeeksChange} 
                                                        />
                                                    </div>

                                                </div>
                                                <hr />
                                                                                                                                                                  
                                                {weekList.weeks?.length > 0 ? renderWeekList() : null}
                                                                      
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

export default Admin_Dashboard;