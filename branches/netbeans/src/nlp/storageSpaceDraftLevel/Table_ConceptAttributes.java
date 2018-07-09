/*
 * structure of attributes of this concept, such as negation,
 * temporality, experiencer
 */

package nlp.storageSpaceDraftLevel;

import commons.Constant;
/**Structure of attributes of this concept, such as negation,
 * temporality, experiencer.<p>
 *
 * @author Jianwei Leng
 * @since Monday 06-07-2010, 12:43 am MST
 */
public class Table_ConceptAttributes {
    public Constant.attributeType type;
    public int hasClassMentionID;
    public String mentionSlotID;
    public String slotMentionValue;
}
