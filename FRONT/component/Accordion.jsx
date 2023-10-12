import React, { useState } from 'react';
import styled, { keyframes } from 'styled-components';
import { BiSolidDownArrow, BiSolidUpArrow } from "react-icons/bi";
import * as cst from '../component/Component'



// OnClick extends card (card containing users and worktime)
const Accordion = ({children, weekNumber}) => {
  const [isVisible, setVisible] = useState(false);

  const handleClick = () => {
      setVisible(!isVisible);    
  };

  return (
    <cst.CardContainer onClick={handleClick} >
        <cst.CardTitle>Semaine {weekNumber} {' '} 
          {!isVisible ? (
          <cst.CardArrow>
            <BiSolidDownArrow/>
          </cst.CardArrow>

          ) : (

          <cst.CardArrow>
            <BiSolidUpArrow/>
          </cst.CardArrow>
          )} 
        </cst.CardTitle>
        {isVisible ? children : null}
    </cst.CardContainer>
  )
};

export default Accordion;
