import { useEffect, useState } from "react";
import { getMySkills,deleteMySkill  } from "../api/skillApi";
import AddSkillForm from "../components/AddSkillForm";
import EditSkillForm from "../components/EditSkillForm";
import { useNavigate } from "react-router-dom";

function MySkills() {
  const [skills, setSkills] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [editingSkillId, setEditingSkillId] = useState(null);
  const navigate = useNavigate();

  async function loadSkills() {
    try {
      setError("");

      const data = await getMySkills();

      setSkills(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }
  

  useEffect(() => {
    loadSkills();
  }, []);

  function handleEdit(skillId) {
    setEditingSkillId(skillId);
  }

  function handleCancelEdit() {
    setEditingSkillId(null);
  }

  async function handleSkillUpdated() {
    setEditingSkillId(null);
    await loadSkills();
  }
  async function handleDelete(skillId, skillName) {
  const confirmed = window.confirm(
    `Are you sure you want to remove ${skillName} from your skills?`
  );

  if (!confirmed) {
    return;
  }

  try {
    setError("");

    await deleteMySkill(skillId);

    await loadSkills();
  } catch (err) {
    setError(err.message);
  }
}

  if (loading) {
    return <p className="page-loading">Loading skills...</p>;
  }

  return (
    <div className="page-container skills-page">
      <button type="button" className="btn btn-outline-primary mb-3" onClick={()=>navigate("/dashboard")}> ← Back to Dashboard</button>
      <h1 className="page-title">My Skills</h1>

      <div className="add-skill-card">
        <AddSkillForm onSkillAdded={loadSkills} />
      </div>

      <hr className="section-divider" />

      <h2 className="section-title">My Current Skills</h2>

      {error && <p className="error-message">{error}</p>}

      {skills.length === 0 ? (
        <div className="empty-state">
          <p>No skills added yet.</p>
        </div>
      ) : (
        <div className="skills-list">
          {skills.map((skill) => (
            <div className="skill-card" key={skill.skillId}>
              <div className="skill-card-header">
                <div>
                  <h3>{skill.skillName}</h3>
                  <p className="skill-category">
                    {skill.category}
                  </p>
                </div>

                <span className="proficiency-badge">
                  {skill.proficiency}/5
                </span>
              </div>

              {editingSkillId === skill.skillId ? (
                <EditSkillForm
                  skill={skill}
                  onUpdated={handleSkillUpdated}
                  onCancel={handleCancelEdit}
                />
              ) : (
                <>
                  <div className="proficiency-section">
                    <span>Proficiency</span>

                    <div className="proficiency-bar">
                      <div
                        className="proficiency-fill"
                        style={{
                          width: `${skill.proficiency * 20}%`,
                        }}
                      ></div>
                    </div>
                  </div>

                  <div className="skill-actions">
                    <button
                      type="button"
                      className="btn btn-edit"
                      onClick={() => handleEdit(skill.skillId)}
                    >
                      Edit
                    </button>

                    <button
                      type="button"
                      className="btn btn-delete"
                      onClick={() =>
                        handleDelete(
                          skill.skillId,
                          skill.skillName
                        )
                      }
                    >
                      Delete
                    </button>
                  </div>
                </>
              )}
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

export default MySkills;