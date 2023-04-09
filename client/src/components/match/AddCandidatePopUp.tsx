import styled from 'styled-components';
import { S_NameTag } from '../UI/S_Tag';
import { Candidate, TeamList } from '../../pages/club/match/CreateMatch';
import { MemberUser } from '../../pages/club/setting/_totalMember';
import { postFetch } from '../../util/api';
import getGlobalState from '../../util/authorization/getGlobalState';
import { useParams } from 'react-router-dom';

interface AddCandidatePopUpProps {
  top: number;
  left: number;
  totalMembers?: MemberUser[];
  candidates?: Candidate[];
  setIsOpenAddMember: React.Dispatch<React.SetStateAction<boolean>>;
}

const S_PopupContainer = styled.div<{ top?: number; left?: number }>`
  position: absolute;
  width: 200px;
  height: auto;
  border-radius: 5px;
  z-index: 9;
  background-color: var(--white);
  top: ${(props) => (props.top ? props.top + 20 : 0)}px;
  left: ${(props) => (props.left ? props.left - 210 : 0)}px;
  padding: 10px;
  box-shadow: rgba(0, 0, 0, 0.16) 0px 1px 4px;
`;

function AddCandidatePopUp(props: AddCandidatePopUpProps) {
  const copiedTotalMember = [...(props.totalMembers as MemberUser[])];
  const { tokens } = getGlobalState();
  const { id, scid } = useParams();

  const filtered = copiedTotalMember.filter(
    (el) => !props.candidates?.map((el) => el.userId)?.includes(el.userId)
  );

  const attendSchedule = (userId: number) => {
    postFetch(
      `${process.env.REACT_APP_URL}/clubs/${id}/schedules/${scid}/users/${userId}/attend`,
      {
        userId,
        scheduleId: scid,
        clubId: id
      },
      tokens
    );
  };

  return (
    <S_PopupContainer top={props.top} left={props.left}>
      {filtered &&
        filtered.map((member, idx) => {
          return (
            <S_NameTag
              key={idx}
              onClick={() => {
                attendSchedule(member.userId);
              }}
            >
              {member.nickName}+
            </S_NameTag>
          );
        })}
    </S_PopupContainer>
  );
}

export default AddCandidatePopUp;
